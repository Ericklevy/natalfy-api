package com.natalfy.application.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MedicoRequestDTOTest {

    private static Validator validator; // O "Juiz" das anotações @NotBlank e @Email

    // Sobe o "Juiz" uma única vez antes de rodar os testes
    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Deve passar sem erros quando o DTO for perfeito")
    void devePassarQuandoDtoValido() {
        MedicoRequestDTO dto = new MedicoRequestDTO("Dr. House", "123456", "Diagnóstico", "house@natalfy.com", "11999999999");

        // Manda o Juiz avaliar o DTO
        Set<ConstraintViolation<MedicoRequestDTO>> violacoes = validator.validate(dto);

        // Garante que a lista de erros está vazia (Zero violações)
        assertTrue(violacoes.isEmpty());
    }

    @Test
    @DisplayName("Deve apontar erro quando o e-mail estiver em formato inválido")
    void deveFalharQuandoEmailInvalido() {
        // Criando DTO com o e-mail faltando o "@"
        MedicoRequestDTO dto = new MedicoRequestDTO("Dr. House", "123456", "Diagnóstico", "email-invalido", "11999999999");

        Set<ConstraintViolation<MedicoRequestDTO>> violacoes = validator.validate(dto);

        // Garante que achou 1 erro
        assertEquals(1, violacoes.size());

        // Garante que a mensagem de erro é exatamente a que escrevemos no DTO
        assertEquals("Formato de e-mail inválido", violacoes.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Deve apontar erro quando o CRM estiver em branco")
    void deveFalharQuandoCrmEmBranco() {
        MedicoRequestDTO dto = new MedicoRequestDTO("Dr. House", "", "Diagnóstico", "house@natalfy.com", "11999999999");

        Set<ConstraintViolation<MedicoRequestDTO>> violacoes = validator.validate(dto);

        assertEquals(1, violacoes.size());
        assertEquals("O CRM é obrigatório", violacoes.iterator().next().getMessage());
    }
}