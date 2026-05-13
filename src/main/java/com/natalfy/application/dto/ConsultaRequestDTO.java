package com.natalfy.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConsultaRequestDTO(
        @NotNull(message = "O ID do médico é obrigatório")
        @Schema(description = "Identificador único do médico", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID medicoId,

        @NotNull(message = "O ID da gestante é obrigatório")
        @Schema(description = "Identificador único da gestante", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID gestanteId,

        @NotNull(message = "A data e hora são obrigatórias")
        @Future(message = "Não é possível agendar uma consulta no passado")
        @Schema(description = "Data e hora da consulta", example = "2024-12-01T14:30:00")
        LocalDateTime dataHora
) {
}