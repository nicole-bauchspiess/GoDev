package med.voll.api.domain.model.dto;

import med.voll.api.domain.model.Endereco;
import med.voll.api.domain.model.Paciente;

public record DadosDetalhamentoPaciente(
        String nome,
        String email,
        String telefone,
       String cpf,
        Endereco endereco) {

    public DadosDetalhamentoPaciente(Paciente paciente){
        this(paciente.getNome(), paciente.getEmail(), paciente.getTelefone(), paciente.getCpf(), paciente.getEndereco());
    }
}
