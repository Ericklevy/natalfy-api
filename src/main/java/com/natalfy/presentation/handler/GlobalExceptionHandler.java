package com.natalfy.presentation.handler;

import com.natalfy.domain.exception.GestanteNaoEncontradaException;
import com.natalfy.domain.exception.MedicoNaoEncontradoException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. ERROS DE VALIDAÇÃO (@Valid, @NotBlank, @CPF...)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação nos dados enviados.",
                erros
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 2. ERROS DE REGRAS DE NEGÓCIO (IllegalArgumentException)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessErrors(IllegalArgumentException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                422,
                ex.getMessage(),
                List.of()
        );
        return ResponseEntity.unprocessableEntity().body(response);
    }

    // 3. ERROS DE NÃO ENCONTRADO (404)
    @ExceptionHandler({GestanteNaoEncontradaException.class, MedicoNaoEncontradoException.class, EntityNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFound(Exception ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage() != null ? ex.getMessage() : "Recurso não encontrado.",
                List.of()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 4. O CATCH-ALL (Proteção contra erros inesperados 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAnyOtherErrors(Exception ex) {
        ex.printStackTrace();

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno no servidor. Por favor, contate o suporte.",
                List.of()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}