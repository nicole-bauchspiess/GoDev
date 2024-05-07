package med.voll.api.domain.model.dto;

import med.voll.api.domain.model.Paciente;

public record DadosListagemPaciente(
        Long id,
        String nome,
        String email,

        String cpf ) {

    public DadosListagemPaciente(Paciente paciente){
        this(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getCpf());
    }

}
