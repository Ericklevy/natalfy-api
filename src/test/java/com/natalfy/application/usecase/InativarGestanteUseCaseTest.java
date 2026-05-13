package com.natalfy.application.usecase;

import com.natalfy.domain.model.Gestante;
import com.natalfy.domain.repository.GestanteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InativarGestanteUseCaseTest {

    @Mock
    private GestanteRepository gestanteRepository;

    @InjectMocks
    private InativarGestanteUseCase useCase;

    @Test
    @DisplayName("Deve inativar a gestante com sucesso")
    void deveInativarGestanteComSucesso() {
        // Arrange
        UUID id = UUID.randomUUID();
        Gestante gestanteAtiva = Gestante.builder()
                .id(id)
                .ativo(true) // Ela começa ativa
                .build();

        when(gestanteRepository.findById(id)).thenReturn(Optional.of(gestanteAtiva));

        // Act
        useCase.executar(id);

        // Assert
        assertFalse(gestanteAtiva.isAtivo()); // Prova que ela foi desligada
        verify(gestanteRepository).save(gestanteAtiva); // Prova que salvou no banco
    }
}