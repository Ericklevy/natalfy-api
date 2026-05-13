package com.natalfy.application.usecase;

import com.natalfy.application.dto.ResumoDashboardDTO;
import com.natalfy.domain.repository.ConsultaRepository;
import com.natalfy.domain.repository.GestanteRepository;
import com.natalfy.domain.repository.MedicoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GerarResumoDashboardUseCaseTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private GestanteRepository gestanteRepository;

    @InjectMocks
    private GerarResumoDashboardUseCase useCase;

    @Test
    @DisplayName("Deve gerar o resumo do dashboard agrupando dados dos repositórios")
    void deveGerarResumoDoDashboard() {
        // Arrange
        // Simulamos que o banco vai devolver 15 consultas hoje, 8 médicos e 120 gestantes
        when(consultaRepository.contarConsultasDoDia(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(15L);
        when(medicoRepository.countByAtivoTrue()).thenReturn(8L);
        when(gestanteRepository.count()).thenReturn(120L);

        // Act
        ResumoDashboardDTO resultado = useCase.executar();

        // Assert
        assertNotNull(resultado);
        assertEquals(15L, resultado.totalConsultasHoje());
        assertEquals(8L, resultado.medicosAtivos());
        assertEquals(120L, resultado.totalGestantes());
    }
}