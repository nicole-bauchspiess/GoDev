package med.voll.api.domain.model.dto;

import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.model.MotivoCancelamento;

public record DadosCancelamentoConsulta(
        @NotNull
        Long idConsulta,

        @NotNull
        MotivoCancelamento motivo
) {
}
