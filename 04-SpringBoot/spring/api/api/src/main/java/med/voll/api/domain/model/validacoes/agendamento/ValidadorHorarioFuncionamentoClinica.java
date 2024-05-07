package med.voll.api.domain.model.validacoes.agendamento;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.model.dto.DadosAgendamentoConsulta;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ValidadorHorarioFuncionamentoClinica implements ValidadorAgendamentoConsultas{

    public void validar(DadosAgendamentoConsulta dados){
        var dataConsulta = dados.data();
        var domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var antesDaAbertura = dataConsulta.getHour() < 7;
        var depoisEncerramento = dataConsulta.getHour() > 18;

        if(domingo || antesDaAbertura || depoisEncerramento){
            throw new ValidacaoException("Consulta fora do horário de funcionamento da clínica");
        }
    }
}
