package com.natalfy.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// Uma "bandeja" para listar qual campo deu erro e qual foi o motivo
public record ErroValidacaoDTO(
        @Schema(description = "Nome do campo que falhou na validação", example = "cpf")
        String campo, 
        
        @Schema(description = "Mensagem descrevendo o motivo da falha", example = "O CPF informado é inválido")
        String mensagem
) {
}