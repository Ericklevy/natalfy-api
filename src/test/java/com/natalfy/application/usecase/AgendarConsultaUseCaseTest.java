package com.natalfy.application.usecase;

import com.natalfy.application.dto.ConsultaRequestDTO;
import com.natalfy.application.dto.ConsultaResponseDTO;
import com.natalfy.domain.model.Consulta;
import com.natalfy.domain.model.Gestante;
import com.natalfy.domain.model.Medico;
import com.natalfy.domain.model.StatusConsulta;
import com.natalfy.domain.repository.ConsultaRepository;
import com.natalfy.domain.repository.GestanteRepository;
import com.natalfy.domain.repository.MedicoRepository;
import com.natalfy.domain.validator.ValidadorAgendamentoConsulta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher; // ✅ Import do Megafone

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendarConsultaUseCaseTest {

    @Mock private ConsultaRepository consultaRepository;
    @Mock private MedicoRepository medicoRepository;
    @Mock private GestanteRepository gestanteRepository;
    @Mock private List<ValidadorAgendamentoConsulta> validadores;

    @Mock private ApplicationEventPublisher eventPublisher; // ✅ O Megafone Falso para o teste não quebrar!

    @InjectMocks
    private AgendarConsultaUseCase useCase;

    @Test
    @DisplayName("Deve agendar consulta com sucesso quando todos os dados forem válidos")
    void deveAgendarConsultaComSucesso() {
        UUID medicoId = UUID.randomUUID();
        UUID gestanteId = UUID.randomUUID();
        LocalDateTime dataHora = LocalDateTime.now().plusDays(2);

        Medico medicoAtivo = Medico.builder().id(medicoId).nome("Dr. Carlos").ativo(true).build();
        Gestante gestanteAtiva = Gestante.builder().id(gestanteId).nome("Maria").ativo(true).build();

        ConsultaRequestDTO request = new ConsultaRequestDTO(medicoId, gestanteId, dataHora);

        when(medicoRepository.findById(medicoId)).thenReturn(Optional.of(medicoAtivo));
        when(gestanteRepository.findById(gestanteId)).thenReturn(Optional.of(gestanteAtiva));
        when(consultaRepository.existsByMedicoIdAndDataHora(medicoId, dataHora)).thenReturn(false);

        ConsultaResponseDTO response = useCase.executar(request);

        assertNotNull(response);
        assertEquals(StatusConsulta.AGENDADA, response.status());
        verify(consultaRepository).save(any(Consulta.class));
// Importe a classe do evento lá no topo se o IntelliJ pedir!
        verify(eventPublisher).publishEvent(any(com.natalfy.domain.event.ConsultaAgendadaEvent.class));    }

    @Test
    @DisplayName("Deve barrar o agendamento se o médico já tiver consulta no mesmo horário")
    void deveLancarErroQuandoChoqueDeHorario() {
        UUID medicoId = UUID.randomUUID();
        UUID gestanteId = UUID.randomUUID();
        LocalDateTime dataHora = LocalDateTime.now().plusDays(2);

        Medico medicoAtivo = Medico.builder().id(medicoId).ativo(true).build();
        Gestante gestanteAtiva = Gestante.builder().id(gestanteId).ativo(true).build();

        ConsultaRequestDTO request = new ConsultaRequestDTO(medicoId, gestanteId, dataHora);

        when(medicoRepository.findById(medicoId)).thenReturn(Optional.of(medicoAtivo));
        when(gestanteRepository.findById(gestanteId)).thenReturn(Optional.of(gestanteAtiva));
        when(consultaRepository.existsByMedicoIdAndDataHora(medicoId, dataHora)).thenReturn(true);

        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class, () -> {
            useCase.executar(request);
        });

        assertEquals("O médico já possui uma consulta agendada para este horário exato.", erro.getMessage());
        verify(consultaRepository, never()).save(any());
    }
}