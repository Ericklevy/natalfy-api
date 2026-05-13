package com.natalfy.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record MotivoCancelamentoDTO(
        @NotBlank(message = "O motivo do cancelamento é obrigatório")
        @Schema(description = "Justificativa para o cancelamento da consulta", example = "Gestante teve um imprevisto e não poderá comparecer")
        String motivo
) {
}