package com.natalfy.presentation.handler;

import java.time.LocalDateTime;
import java.util.List;

// Esse é o molde do JSON bonito que o Angular vai receber
public record ApiErrorResponse(
        LocalDateTime timestamp,
        Integer status,
        String message,
        List<String> errors // Lista com todos os errinhos (ex: CPF errado, Nome vazio)
) {
}
