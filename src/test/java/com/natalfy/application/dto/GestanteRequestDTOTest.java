package com.natalfy.application.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GestanteRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        // Inicializa o validador padrão do Spring/Hibernate (O leão de chácara)
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    // --- TESTES DE SUCESSO ---

    @Test
    @DisplayName("Deve passar na validação quando todos os dados, incluindo um CPF válido, estiverem corretos")
    void devePassarQuandoTodosOsDadosForemValidos() {
        // O CPF "00000000000" pode ser matematicamente válido em alguns algoritmos simples,
        // mas a anotação @CPF do Hibernate é inteligente e sabe que é um CPF de teste.
        // Vamos usar um CPF válido real gerado para testes: 52998224725
        GestanteRequestDTO request = new GestanteRequestDTO(
                "Amanda Silva", "52998224725", LocalDate.of(1995, 5, 10), "amanda@email.com", "61999999999"
        );

        // Dispara a validação no DTO
        Set<ConstraintViolation<GestanteRequestDTO>> violations = validator.validate(request);

        // Se a lista de violações estiver vazia, significa que o objeto é perfeito!
        assertTrue(violations.isEmpty(), "O DTO deveria ser considerado válido");
    }

    // --- TESTES DE FALHA (MASSACRE DO CPF) ---

    // O @ParameterizedTest é incrível! Ele roda o mesmo teste várias vezes,
    // injetando cada valor do @ValueSource dentro da variável 'cpfInvalido'.
    @ParameterizedTest(name = "Deve bloquear o CPF inválido: {0}")
    @ValueSource(strings = {
            "12345678900",   // CPF matematicamente falso (agora sim!)
            "00000000000",   // Todos os números iguais
            "11111111111",   // Todos os números iguais
            "99999999999",   // Todos os números iguais
            "123",           // CPF muito curto
            "1234567890123", // CPF muito longo
            "ABCDEFGHIJK",   // Contém letras
            "123!456@789",   // Contém caracteres especiais estranhos
            "   ",           // Apenas espaços em branco
            ""               // Vazio
    })
    void deveBarraCpfsInvalidos(String cpfInvalido) {
        GestanteRequestDTO request = new GestanteRequestDTO(
                "Amanda Silva", cpfInvalido, LocalDate.of(1995, 5, 10), "amanda@email.com", "61999999999"
        );

        Set<ConstraintViolation<GestanteRequestDTO>> violations = validator.validate(request);

        // A lista de violações NÃO pode estar vazia (precisa ter achado o erro no CPF)
        assertFalse(violations.isEmpty(), "O validador deveria ter barrado o CPF: " + cpfInvalido);

        // Vamos garantir que a mensagem de erro que ele gerou seja exatamente sobre o CPF
        boolean encontrouErroNoCpf = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("cpf")
                        && (v.getMessage().contains("inválido") || v.getMessage().contains("obrigatório")));

        assertTrue(encontrouErroNoCpf, "Deveria ter apontado um erro específico no campo CPF");
    }

    // --- TESTES EXTRAS DE OUTROS CAMPOS ---

    @Test
    @DisplayName("Deve barrar e-mails com formato inválido")
    void deveBarrarEmailInvalido() {
        GestanteRequestDTO request = new GestanteRequestDTO(
                "Amanda", "52998224725", LocalDate.of(1995, 5, 10), "email-sem-arroba.com", "61999999999"
        );

        Set<ConstraintViolation<GestanteRequestDTO>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    @DisplayName("Deve barrar data de nascimento no futuro")
    void deveBarrarDataNascimentoNoFuturo() {
        GestanteRequestDTO request = new GestanteRequestDTO(
                "Amanda", "52998224725", LocalDate.now().plusDays(1), "amanda@email.com", "61999999999"
        );

        Set<ConstraintViolation<GestanteRequestDTO>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dataNascimento")));
    }
}