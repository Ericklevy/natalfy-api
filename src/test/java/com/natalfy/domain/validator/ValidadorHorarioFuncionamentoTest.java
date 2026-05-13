package com.natalfy.domain.validator;

import com.natalfy.application.dto.ConsultaRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidadorHorarioFuncionamentoTest {

    private final ValidadorHorarioFuncionamento validador = new ValidadorHorarioFuncionamento();

    @Test
    @DisplayName("Deve permitir agendamento em dia de semana e horário comercial")
    void devePermitirAgendamentoValido() {
        // Arrange: Uma segunda-feira às 10:00
        var dataValida = LocalDateTime.of(2026, 5, 18, 10, 0);
        var dto = new ConsultaRequestDTO(UUID.randomUUID(), UUID.randomUUID(), dataValida);

        // Act & Assert: Não deve lançar exceção
        assertDoesNotThrow(() -> validador.validar(dto));
    }

    @Test
    @DisplayName("Não deve permitir agendamento aos domingos")
    void naoDevePermitirAgendamentoNoDomingo() {
        // Arrange: Um domingo às 10:00
        var domingo = LocalDateTime.of(2026, 5, 17, 10, 0);
        var dto = new ConsultaRequestDTO(UUID.randomUUID(), UUID.randomUUID(), domingo);

        // Act & Assert: Deve lançar IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> validador.validar(dto));
    }

    @Test
    @DisplayName("Não deve permitir agendamento antes da abertura (06:00)")
    void naoDevePermitirAgendamentoAntesDasSete() {
        // Arrange: Uma segunda-feira às 06:00
        var muitoCedo = LocalDateTime.of(2026, 5, 18, 6, 0);
        var dto = new ConsultaRequestDTO(UUID.randomUUID(), UUID.randomUUID(), muitoCedo);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> validador.validar(dto));
    }

    @Test
    @DisplayName("Não deve permitir agendamento após o fechamento (19:00+)")
    void naoDevePermitirAgendamentoAposAsDezoito() {
        // Arrange: Uma segunda-feira às 19:30
        var muitoTarde = LocalDateTime.of(2026, 5, 18, 19, 30);
        var dto = new ConsultaRequestDTO(UUID.randomUUID(), UUID.randomUUID(), muitoTarde);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> validador.validar(dto));
    }
}