package com.natalfy.application.usecase;

import com.natalfy.application.dto.ConsultaResponseDTO;
import com.natalfy.application.dto.ProntuarioRequestDTO;
import com.natalfy.domain.model.Consulta;
import com.natalfy.domain.model.Gestante;
import com.natalfy.domain.model.Medico;
import com.natalfy.domain.model.StatusConsulta;
import com.natalfy.domain.repository.ConsultaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinalizarConsultaUseCaseTest {

    @Mock private ConsultaRepository repository;
    @InjectMocks private FinalizarConsultaUseCase useCase;

    @Test
    @DisplayName("Deve finalizar a consulta salvando o prontuário")
    void deveFinalizarConsultaComSucesso() {
        UUID id = UUID.randomUUID();
        Consulta consulta = Consulta.builder()
                .id(id).medico(new Medico()).gestante(new Gestante())
                .status(StatusConsulta.AGENDADA).build();

        ProntuarioRequestDTO dto = new ProntuarioRequestDTO("Paciente apresenta pressão normal. Receitado polivitamínico.");

        when(repository.findById(id)).thenReturn(Optional.of(consulta));

        ConsultaResponseDTO response = useCase.executar(id, dto);

        assertEquals(StatusConsulta.REALIZADA, response.status());
        assertEquals("Paciente apresenta pressão normal. Receitado polivitamínico.", consulta.getProntuario());
        verify(repository).save(consulta);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar finalizar consulta já cancelada")
    void deveLancarErroSeStatusNaoForAgendada() {
        UUID id = UUID.randomUUID();
        Consulta consultaCancelada = Consulta.builder().id(id).status(StatusConsulta.CANCELADA).build();
        ProntuarioRequestDTO dto = new ProntuarioRequestDTO("Tentando finalizar cancelada.");

        when(repository.findById(id)).thenReturn(Optional.of(consultaCancelada));

        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class, () -> useCase.executar(id, dto));
        assertEquals("Apenas consultas com status AGENDADA podem ser finalizadas.", erro.getMessage());
    }
}