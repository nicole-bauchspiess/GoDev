package med.voll.api.domain.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosEndereco(
        @NotBlank(message = "o logradouro não pode ser nulo")
        String logradouro,
        @NotBlank(message = "o bairro não pode ser nulo")
        String bairro,
        @NotBlank(message = "o cep não pode ser nulo")
        @Pattern(regexp = "\\d{8}")
        String cep,
        @NotBlank(message = "o cidade não pode ser nulo")
        String cidade,
        @NotBlank(message = "o uf não pode ser nulo")
        String uf,
        String complemento,
        String numero) {
}
