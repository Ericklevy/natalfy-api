package com.natalfy.application.usecase;

import com.natalfy.application.dto.MotivoCancelamentoDTO;
import com.natalfy.application.dto.ConsultaResponseDTO;
import com.natalfy.domain.model.Consulta;
import com.natalfy.domain.model.Gestante;
import com.natalfy.domain.model.Medico;
import com.natalfy.domain.model.StatusConsulta;
import com.natalfy.domain.repository.ConsultaRepository;
import com.natalfy.domain.validator.ValidadorCancelamentoConsulta; // Interface que vamos criar
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelarConsultaUseCaseTest {

    @Mock
    private ConsultaRepository repository;

    @Mock
    private List<ValidadorCancelamentoConsulta> validadores; // A lista de guardas do cancelamento!

    @InjectMocks
    private CancelarConsultaUseCase useCase;

    @Test
    @DisplayName("Deve cancelar a consulta com sucesso e passar pelos validadores")
    void deveCancelarConsulta() {
        // Arrange
        UUID consultaId = UUID.randomUUID();
        MotivoCancelamentoDTO motivoDTO = new MotivoCancelamentoDTO("Imprevisto familiar");

        Medico medico = Medico.builder().id(UUID.randomUUID()).nome("Dr. Carlos").build();
        Gestante gestante = Gestante.builder().id(UUID.randomUUID()).nome("Maria").build();

        Consulta consulta = Consulta.builder()
                .id(consultaId)
                .medico(medico)
                .gestante(gestante)
                .dataHora(LocalDateTime.now().plusDays(2))
                .status(StatusConsulta.AGENDADA)
                .build();

        when(repository.findById(consultaId)).thenReturn(Optional.of(consulta));

        // Act
        ConsultaResponseDTO response = useCase.executar(consultaId, motivoDTO);

        // Assert
        assertEquals(StatusConsulta.CANCELADA, response.status());
        verify(validadores).forEach(any()); // Garante que chamou os validadores!
    }
}