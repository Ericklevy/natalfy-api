package com.natalfy.domain.validator;

import com.natalfy.application.dto.ConsultaRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidadorHorarioAntecedenciaTest {

    private final ValidadorHorarioAntecedencia validador = new ValidadorHorarioAntecedencia();

    @Test
    @DisplayName("Deve permitir agendamento com mais de 30 minutos de antecedência")
    void devePermitirComAntecedencia() {
        // Arrange: Agendando para daqui a 2 horas
        var dataValida = LocalDateTime.now().plusHours(2);
        var dto = new ConsultaRequestDTO(UUID.randomUUID(), UUID.randomUUID(), dataValida);

        // Act & Assert
        assertDoesNotThrow(() -> validador.validar(dto));
    }

    @Test
    @DisplayName("Não deve permitir agendamento com menos de 30 minutos de antecedência")
    void naoDevePermitirSemAntecedencia() {
        // Arrange: Agendando para daqui a apenas 10 minutos
        var dataInvalida = LocalDateTime.now().plusMinutes(10);
        var dto = new ConsultaRequestDTO(UUID.randomUUID(), UUID.randomUUID(), dataInvalida);

        // Act & Assert: Deve bloquear
        assertThrows(IllegalArgumentException.class, () -> validador.validar(dto));
    }
}