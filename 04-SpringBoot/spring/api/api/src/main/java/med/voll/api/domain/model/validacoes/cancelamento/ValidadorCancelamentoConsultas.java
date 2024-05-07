package med.voll.api.domain.model.validacoes.cancelamento;

import med.voll.api.domain.model.dto.DadosCancelamentoConsulta;

public interface ValidadorCancelamentoConsultas {

    void validar(DadosCancelamentoConsulta dados);
}
