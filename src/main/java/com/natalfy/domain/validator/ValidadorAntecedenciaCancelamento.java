package com.natalfy.domain.validator;

import com.natalfy.domain.model.Consulta;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorAntecedenciaCancelamento implements ValidadorCancelamentoConsulta {

    @Override
    public void validar(Consulta consulta) {
        var agora = LocalDateTime.now();
        var diferencaEmHoras = Duration.between(agora, consulta.getDataHora()).toHours();

        if (diferencaEmHoras < 24) {
            throw new IllegalArgumentException("A consulta só pode ser cancelada com antecedência mínima de 24h.");
        }
    }
}