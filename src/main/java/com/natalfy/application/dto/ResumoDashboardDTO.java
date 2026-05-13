package com.natalfy.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResumoDashboardDTO(
        @Schema(description = "Total de consultas agendadas para o dia atual", example = "15")
        Long totalConsultasHoje,
        
        @Schema(description = "Quantidade de médicos ativos no sistema", example = "10")
        Long medicosAtivos,
        
        @Schema(description = "Quantidade total de gestantes cadastradas", example = "150")
        Long totalGestantes
) {
}