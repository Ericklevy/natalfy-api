package com.natalfy.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record GestanteRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Schema(description = "Nome completo da gestante", example = "Maria da Silva")
        String nome,

        @NotBlank(message = "O CPF é obrigatório")
        @CPF(message = "O CPF informado é inválido")
        @Schema(description = "CPF da gestante", example = "123.456.789-00")
        String cpf,

        @NotNull(message = "A data de nascimento é obrigatória")
        @Past(message = "A data de nascimento deve estar no passado")
        @Schema(description = "Data de nascimento da gestante no formato YYYY-MM-DD", example = "1990-05-15")
        LocalDate dataNascimento,

        @NotBlank(message = "O E-mail é obrigatório")
        @Email(message = "O formato do e-mail é inválido")
        @Schema(description = "Endereço de e-mail da gestante", example = "maria.silva@email.com")
        String email,

        @Schema(description = "Telefone para contato", example = "(11) 98765-4321")
        String telefone
) {
}