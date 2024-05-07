package med.voll.api.domain.service;

import jakarta.transaction.Transactional;
import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.model.Consulta;
import med.voll.api.domain.model.Medico;
import med.voll.api.domain.model.dto.DadosAgendamentoConsulta;
import med.voll.api.domain.model.dto.DadosCancelamentoConsulta;
import med.voll.api.domain.model.dto.DadosDetalhamentoConsulta;
import med.voll.api.domain.model.validacoes.agendamento.ValidadorAgendamentoConsultas;
import med.voll.api.domain.model.validacoes.cancelamento.ValidadorCancelamentoConsultas;
import med.voll.api.domain.repository.ConsultaRepository;
import med.voll.api.domain.repository.MedicoRepository;
import med.voll.api.domain.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultasService {


    @Autowired
    private ConsultaRepository repository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private List<ValidadorAgendamentoConsultas> validadores; //spring pega todas as classes que implementam o validador e colocam na lista

    @Autowired
    private List<ValidadorCancelamentoConsultas> validadoresCancelamento;


    public DadosDetalhamentoConsulta agendarConsultas(DadosAgendamentoConsulta dados){
        if(!pacienteRepository.existsById(dados.idPaciente())){
            throw new ValidacaoException("Id do paciente informado não existe");
        }
        if(dados.idMedico()!= null && !medicoRepository.existsById(dados.idMedico())){
            throw new ValidacaoException("Id do médico informado não existe");
        }

        validadores.forEach(v-> v.validar(dados));

        var medico = escolherMedico(dados);
        if(medico==null){
            throw new ValidacaoException("Nao existe medico disponivel nesta data");
        }
        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());

        var consulta = new Consulta(null, medico, paciente, dados.data(), null);
        repository.save(consulta);

        return new DadosDetalhamentoConsulta(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if(dados.idMedico()!= null){
            return medicoRepository.getReferenceById(dados.idMedico());
        }

        if(dados.especialidade()==null){
            throw new ValidacaoException("Especialidade é obrigatória quando o médico não é escolhido");
        }
        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());

    }


    public void cancelar(DadosCancelamentoConsulta dados) {
        if(!repository.existsById(dados.idConsulta())){
            throw new ValidacaoException("Id da consulta informada não foi encontrado");
        }

        validadoresCancelamento.forEach(v -> v.validar(dados));
        var consulta = repository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());
    }




}
