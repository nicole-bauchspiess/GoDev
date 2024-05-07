package med.voll.api.domain.model.validacoes.agendamento;

import med.voll.api.domain.model.dto.DadosAgendamentoConsulta;

public interface ValidadorAgendamentoConsultas {

    void validar(DadosAgendamentoConsulta dados);
}
