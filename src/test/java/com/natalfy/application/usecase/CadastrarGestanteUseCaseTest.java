package com.natalfy.application.usecase;

import com.natalfy.application.dto.GestanteRequestDTO;
import com.natalfy.application.dto.GestanteResponseDTO;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Liga os superpoderes do Mockito
class CadastrarGestanteUseCaseTest {

    @Mock
    private GestanteRepository gestanteRepository; // Finge que é o banco de dados

    @InjectMocks
    private CadastrarGestanteUseCase useCase; // A classe real que estamos testando

    @Test
    @DisplayName("Deve cadastrar uma gestante com sucesso quando os dados forem válidos")
    void deveCadastrarGestanteComSucesso() {
        // 1. Arrange (Preparar o cenário)
        GestanteRequestDTO request = new GestanteRequestDTO(
                "Amanda Silva", "12345678909", LocalDate.of(1995, 5, 10), "amanda@email.com", "61999999999"
        );

        Gestante gestanteSimuladaSalva = Gestante.builder()
                .id(UUID.randomUUID())
                .nome(request.nome())
                .cpf(request.cpf())
                .email(request.email())
                .dataCadastro(LocalDate.now())
                .build();

        // Ensinando o banco de dados de mentira a responder
        when(gestanteRepository.findByCpf(request.cpf())).thenReturn(Optional.empty()); // CPF não existe ainda
        when(gestanteRepository.findByEmail(request.email())).thenReturn(Optional.empty()); // Email não existe ainda
        when(gestanteRepository.save(any(Gestante.class))).thenReturn(gestanteSimuladaSalva); // Retorna a gestante salva

        // 2. Act (Agir - Chamar o método real)
        GestanteResponseDTO response = useCase.executar(request);

        // 3. Assert (Verificar se aconteceu o que esperávamos)
        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals("Amanda Silva", response.nome());

        // Verifica se o método save() do repositório foi chamado exatamente 1 vez
        verify(gestanteRepository, times(1)).save(any(Gestante.class));
    }

    @Test
    @DisplayName("Deve bloquear o cadastro e lançar erro se o CPF já existir")
    void deveLancarErroQuandoCpfJaExistir() {
        // 1. Arrange
        GestanteRequestDTO request = new GestanteRequestDTO(
                "Amanda Silva", "12345678909", LocalDate.of(1995, 5, 10), "amanda@email.com", "61999999999"
        );

        // Ensinando o banco: "Quando procurarem esse CPF, diga que já tem alguém com ele!"
        when(gestanteRepository.findByCpf(request.cpf())).thenReturn(Optional.of(new Gestante()));

        // 2 e 3. Act e Assert (Agir e Verificar o Erro)
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class, () -> {
            useCase.executar(request);
        });

        assertEquals("Já existe uma gestante cadastrada com este CPF.", erro.getMessage());

        // Verifica que o método save() NUNCA foi chamado, blindando o banco!
        verify(gestanteRepository, never()).save(any(Gestante.class));
    }
}