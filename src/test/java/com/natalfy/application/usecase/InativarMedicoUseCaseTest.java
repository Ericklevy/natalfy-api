package com.natalfy.application.usecase;

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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InativarMedicoUseCaseTest {

    @Mock
    private MedicoRepository medicoRepository;

    @InjectMocks
    private InativarMedicoUseCase useCase;

    @Test
    @DisplayName("Deve inativar o médico com sucesso")
    void deveInativarMedicoComSucesso() {
        UUID id = UUID.randomUUID();
        Medico medicoAtivo = Medico.builder()
                .id(id).ativo(true).build();

        when(medicoRepository.findById(id)).thenReturn(Optional.of(medicoAtivo));

        useCase.executar(id);

        assertFalse(medicoAtivo.isAtivo()); // Prova que o botão de desliga funcionou
        verify(medicoRepository).save(medicoAtivo); // Prova que mandou salvar a inativação
    }
}