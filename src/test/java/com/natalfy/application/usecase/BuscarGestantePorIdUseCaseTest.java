package com.natalfy.application.usecase;

import com.natalfy.application.dto.GestanteResponseDTO;
import com.natalfy.domain.exception.GestanteNaoEncontradaException;
import com.natalfy.domain.model.Gestante;
import com.natalfy.domain.repository.GestanteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarGestantePorIdUseCaseTest {

    @Mock
    private GestanteRepository gestanteRepository;

    @InjectMocks
    private BuscarGestantePorIdUseCase useCase;

    @Test
    @DisplayName("Deve retornar a gestante quando o ID existir")
    void deveRetornarGestanteQuandoIdExistir() {
        UUID idValido = UUID.randomUUID();
        Gestante gestanteSimulada = Gestante.builder().id(idValido).nome("Maria").email("maria@email.com").dataCadastro(LocalDate.now()).build();

        when(gestanteRepository.findById(idValido)).thenReturn(Optional.of(gestanteSimulada));

        GestanteResponseDTO response = useCase.executar(idValido);

        assertNotNull(response);
        assertEquals("Maria", response.nome());
    }

    @Test
    @DisplayName("Deve lançar erro 404 quando o ID não existir")
    void deveLancarErroQuandoIdNaoExistir() {
        UUID idInvalido = UUID.randomUUID();
        when(gestanteRepository.findById(idInvalido)).thenReturn(Optional.empty());

        GestanteNaoEncontradaException erro = assertThrows(GestanteNaoEncontradaException.class, () -> {
            useCase.executar(idInvalido);
        });

        assertEquals("Gestante não encontrada com o ID informado.", erro.getMessage());
    }
}