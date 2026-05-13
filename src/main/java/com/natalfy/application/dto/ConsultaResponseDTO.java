package com.natalfy.application.dto;

import com.natalfy.domain.model.StatusConsulta;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConsultaResponseDTO(
        @Schema(description = "Identificador único da consulta", example = "990e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Identificador único do médico", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID medicoId,

        @Schema(description = "Nome do médico", example = "Dr. João Silva")
        String nomeMedico,

        @Schema(description = "Identificador único da gestante", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID gestanteId,

        @Schema(description = "Nome da gestante", example = "Maria da Silva")
        String nomeGestante,

        @Schema(description = "Data e hora da consulta agendada", example = "2024-12-01T14:30:00")
        LocalDateTime dataHora,

        @Schema(description = "Status atual da consulta", example = "AGENDADA")
        StatusConsulta status
) {
}