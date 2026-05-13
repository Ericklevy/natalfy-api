package com.natalfy.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MedicoUpdateRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Schema(description = "Nome completo do médico", example = "Dr. João Silva Atualizado")
        String nome,

        @NotBlank(message = "A especialidade é obrigatória")
        @Schema(description = "Nova especialidade médica", example = "Ginecologia e Obstetrícia")
        String especialidade,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        @Schema(description = "Novo e-mail do médico", example = "joao.silva.novo@clinica.com")
        String email,

        @Schema(description = "Novo telefone para contato", example = "(11) 97777-6666")
        String telefone
) {
}