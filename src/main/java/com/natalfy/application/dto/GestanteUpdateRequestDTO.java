package com.natalfy.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record GestanteUpdateRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Schema(description = "Nome completo da gestante", example = "Maria da Silva Atualizada")
        String nome,

        @NotNull(message = "A data de nascimento é obrigatória")
        @Schema(description = "Data de nascimento da gestante", example = "1990-05-15")
        LocalDate dataNascimento,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        @Schema(description = "Novo endereço de e-mail", example = "maria.nova@email.com")
        String email,

        @NotBlank(message = "O telefone é obrigatório")
        @Schema(description = "Novo telefone para contato", example = "(11) 91234-5678")
        String telefone
) {
}