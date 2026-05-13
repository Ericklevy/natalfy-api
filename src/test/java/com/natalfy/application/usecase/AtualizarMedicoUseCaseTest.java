package com.natalfy.application.usecase;

import com.natalfy.application.dto.MedicoResponseDTO;
import com.natalfy.application.dto.MedicoUpdateRequestDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarMedicoUseCaseTest {

    @Mock
    private MedicoRepository medicoRepository;

    @InjectMocks
    private AtualizarMedicoUseCase useCase;

    @Test
    @DisplayName("Deve atualizar os dados permitidos do médico com sucesso")
    void deveAtualizarMedicoComSucesso() {
        UUID id = UUID.randomUUID();
        Medico medicoAntigo = Medico.builder()
                .id(id).nome("Dr. Carlos").crm("123456").especialidade("Obstetrícia").email("carlos.velho@email.com").telefone("111111111").build();

        // O request não manda CRM, pois CRM não se altera!
        MedicoUpdateRequestDTO novosDados = new MedicoUpdateRequestDTO(
                "Dr. Carlos Atualizado", "Obstetrícia", "carlos.novo@email.com", "999999999"
        );

        when(medicoRepository.findById(id)).thenReturn(Optional.of(medicoAntigo));
        when(medicoRepository.save(any(Medico.class))).thenReturn(medicoAntigo);

        MedicoResponseDTO response = useCase.executar(id, novosDados);

        assertEquals("Dr. Carlos Atualizado", response.nome());
        assertEquals("carlos.novo@email.com", response.email());
        assertEquals("123456", response.crm()); // Garante que o CRM continua intacto

        verify(medicoRepository).save(medicoAntigo); // Garante a ida ao banco
    }
}