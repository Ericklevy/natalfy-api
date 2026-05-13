package com.natalfy.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record MedicoResponseDTO(
        @Schema(description = "Identificador único do médico", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,
        
        @Schema(description = "Nome completo do médico", example = "Dr. João Silva")
        String nome,
        
        @Schema(description = "Registro no Conselho Regional de Medicina", example = "CRM-SP 123456")
        String crm,
        
        @Schema(description = "Especialidade médica", example = "Obstetrícia")
        String especialidade,
        
        @Schema(description = "Endereço de e-mail do médico", example = "joao.silva@clinica.com")
        String email
) {
}