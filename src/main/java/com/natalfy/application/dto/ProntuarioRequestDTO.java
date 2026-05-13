package com.natalfy.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ProntuarioRequestDTO(
        @NotBlank(message = "O prontuário com as anotações do médico é obrigatório para finalizar a consulta.")
        @Schema(description = "Anotações do médico sobre a consulta", example = "Paciente apresentou melhora nos sintomas. Exames solicitados.")
        String prontuario
) {
}