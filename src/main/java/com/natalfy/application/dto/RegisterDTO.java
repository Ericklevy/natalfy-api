package com.natalfy.application.dto;

import com.natalfy.domain.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
        @NotBlank(message = "O e-mail (login) é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        @Schema(description = "E-mail de login para o novo usuário", example = "novo.usuario@email.com")
        String login,

        @NotBlank(message = "A senha é obrigatória")
        @Schema(description = "Senha de acesso do novo usuário", example = "senhaSegura123!")
        String password,

        @NotNull(message = "O perfil de acesso (role) é obrigatório")
        @Schema(description = "Perfil de acesso do usuário (ex: ADMIN, USER)", example = "USER")
        UserRole role
) {
}