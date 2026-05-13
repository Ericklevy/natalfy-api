package com.natalfy.application.usecase;

import com.natalfy.application.dto.GestanteResponseDTO;
import com.natalfy.application.dto.GestanteUpdateRequestDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarGestanteUseCaseTest {

    @Mock
    private GestanteRepository gestanteRepository;

    @InjectMocks
    private AtualizarGestanteUseCase useCase;

    @Test
    @DisplayName("Deve atualizar os dados da gestante com sucesso")
    void deveAtualizarGestanteComSucesso() {
        // Arrange
        UUID id = UUID.randomUUID();
        Gestante gestanteAntiga = Gestante.builder()
                .id(id).nome("Maria").email("maria@antigo.com").telefone("111111111")
                .dataNascimento(LocalDate.of(1990, 1, 1)).dataCadastro(LocalDate.now())
                .build();

        GestanteUpdateRequestDTO novosDados = new GestanteUpdateRequestDTO(
                "Maria Atualizada", LocalDate.of(1990, 1, 1), "maria.nova@email.com", "999999999"
        );

        when(gestanteRepository.findById(id)).thenReturn(Optional.of(gestanteAntiga));
        when(gestanteRepository.save(any(Gestante.class))).thenReturn(gestanteAntiga);

        // Act
        GestanteResponseDTO response = useCase.executar(id, novosDados);

        // Assert
        assertEquals("Maria Atualizada", response.nome());
        assertEquals("maria.nova@email.com", response.email());
        verify(gestanteRepository).save(gestanteAntiga); // Garante que mandou salvar
    }
}