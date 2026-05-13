package com.natalfy.application.usecase;

import com.natalfy.application.dto.MedicoResponseDTO;
import com.natalfy.domain.exception.MedicoNaoEncontradoException;
import com.natalfy.domain.model.Medico;
import com.natalfy.domain.repository.MedicoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarMedicoPorIdUseCaseTest {

    @Mock
    private MedicoRepository medicoRepository;

    @InjectMocks
    private BuscarMedicoPorIdUseCase useCase;

    @Test
    @DisplayName("Deve retornar o médico quando o ID existir")
    void deveRetornarMedicoQuandoIdExistir() {
        UUID idValido = UUID.randomUUID();
        Medico medicoSimulado = Medico.builder()
                .id(idValido).nome("Dr. Carlos").crm("123456").especialidade("Obstetrícia").email("carlos@email.com").ativo(true).build();

        when(medicoRepository.findById(idValido)).thenReturn(Optional.of(medicoSimulado));

        MedicoResponseDTO response = useCase.executar(idValido);

        assertNotNull(response);
        assertEquals("Dr. Carlos", response.nome());
        assertEquals("123456", response.crm());
    }

    @Test
    @DisplayName("Deve lançar erro 404 quando o ID do médico não existir")
    void deveLancarErroQuandoIdNaoExistir() {
        UUID idInvalido = UUID.randomUUID();
        when(medicoRepository.findById(idInvalido)).thenReturn(Optional.empty());

        MedicoNaoEncontradoException erro = assertThrows(MedicoNaoEncontradoException.class, () -> {
            useCase.executar(idInvalido);
        });

        assertEquals("Médico não encontrado.", erro.getMessage());
    }
}