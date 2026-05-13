package com.natalfy.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDTO(
        @Schema(description = "Token JWT gerado após o login com sucesso", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJtYXJpYS5zaWx2YUBlbWFpbC5jb20iLCJyb2xlcyI6W... ")
        String token
) {
}