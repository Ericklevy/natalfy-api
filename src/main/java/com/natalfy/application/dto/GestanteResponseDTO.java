package com.natalfy.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.UUID;

public record GestanteResponseDTO(
        @Schema(description = "Identificador único da gestante", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        
        @Schema(description = "Nome completo da gestante", example = "Maria da Silva")
        String nome,
        
        @Schema(description = "Endereço de e-mail", example = "maria.silva@email.com")
        String email,
        
        @Schema(description = "Data de cadastro no sistema", example = "2023-10-25")
        LocalDate dataCadastro
) {
}