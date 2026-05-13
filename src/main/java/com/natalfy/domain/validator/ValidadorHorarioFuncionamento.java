package com.natalfy.domain.validator;

import com.natalfy.application.dto.ConsultaRequestDTO;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ValidadorHorarioFuncionamento implements ValidadorAgendamentoConsulta {

    @Override
    public void validar(ConsultaRequestDTO dados) {
        var dataConsulta = dados.dataHora();

        var domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var antesDaAbertura = dataConsulta.getHour() < 7;
        var depoisDoFechamento = dataConsulta.getHour() > 18; // 18h é o último horário aceito

        if (domingo || antesDaAbertura || depoisDoFechamento) {
            throw new IllegalArgumentException("Consulta fora do horário de funcionamento (Seg-Sáb, 07:00 às 19:00)");
        }
    }
}