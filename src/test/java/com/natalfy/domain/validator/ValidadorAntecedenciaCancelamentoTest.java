package com.natalfy.domain.validator;

import com.natalfy.domain.model.Consulta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidadorAntecedenciaCancelamentoTest {

    // O IntelliJ vai reclamar que essa classe não existe (ainda!)
    private final ValidadorAntecedenciaCancelamento validador = new ValidadorAntecedenciaCancelamento();

    @Test
    @DisplayName("Deve permitir cancelamento com mais de 24 horas de antecedência")
    void devePermitirCancelamentoAntecipado() {
        // Arrange: Consulta daqui a 2 dias
        Consulta consulta = Consulta.builder()
                .dataHora(LocalDateTime.now().plusDays(2))
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> validador.validar(consulta));
    }

    @Test
    @DisplayName("Não deve permitir cancelamento com menos de 24 horas de antecedência")
    void naoDevePermitirCancelamentoEmCimaDaHora() {
        // Arrange: Consulta para daqui a apenas 10 horas
        Consulta consulta = Consulta.builder()
                .dataHora(LocalDateTime.now().plusHours(10))
                .build();

        // Act & Assert: Deve lançar a nossa exceção de regra de negócio
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class, () -> validador.validar(consulta));
        org.junit.jupiter.api.Assertions.assertEquals("A consulta só pode ser cancelada com antecedência mínima de 24h.", erro.getMessage());
    }
}