package com.natalfy.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @NotBlank(message = "O e-mail (login) é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        @Schema(description = "E-mail cadastrado pelo usuário", example = "maria.silva@email.com")
        String login,

        @NotBlank(message = "A senha é obrigatória")
        @Schema(description = "Senha do usuário", example = "senhaForte123!")
        String password
) {
}