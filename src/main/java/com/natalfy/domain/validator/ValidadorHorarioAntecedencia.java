package com.natalfy.domain.validator;

import com.natalfy.application.dto.ConsultaRequestDTO;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioAntecedencia implements ValidadorAgendamentoConsulta {

    @Override
    public void validar(ConsultaRequestDTO dados) {
        var dataConsulta = dados.dataHora();
        var agora = LocalDateTime.now();
        var diferencaEmMinutos = Duration.between(agora, dataConsulta).toMinutes();

        if (diferencaEmMinutos < 30) {
            throw new IllegalArgumentException("Consulta deve ser agendada com no mínimo 30 minutos de antecedência.");
        }
    }
}