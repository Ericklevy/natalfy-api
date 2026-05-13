package com.natalfy.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MedicoRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Schema(description = "Nome completo do médico", example = "Dr. João Silva")
        String nome,

        @NotBlank(message = "O CRM é obrigatório")
        @Schema(description = "Registro no Conselho Regional de Medicina", example = "CRM-SP 123456")
        String crm,

        @NotBlank(message = "A especialidade é obrigatória")
        @Schema(description = "Especialidade médica", example = "Obstetrícia")
        String especialidade,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        @Schema(description = "Endereço de e-mail do médico", example = "joao.silva@clinica.com")
        String email,

        @Schema(description = "Telefone para contato", example = "(11) 98888-7777")
        String telefone // Telefone não tem @NotBlank, pode ser opcional se a clínica permitir
) {
}