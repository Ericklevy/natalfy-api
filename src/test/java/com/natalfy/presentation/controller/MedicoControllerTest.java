package com.natalfy.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.natalfy.application.dto.MedicoRequestDTO;
import com.natalfy.application.dto.MedicoResponseDTO;
import com.natalfy.application.usecase.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

// ✅ Pacotes novos do Spring Boot 4 (Iguais aos da Gestante!)
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicoController.class)
@AutoConfigureMockMvc(addFilters = false) // Desliga a segurança do Spring Security pro teste passar
class MedicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ Usando o @MockitoBean do Spring Boot 4
    @MockitoBean private CadastrarMedicoUseCase cadastrarMedicoUseCase;
    @MockitoBean private ListarMedicosUseCase listarMedicosUseCase;
    @MockitoBean private BuscarMedicoPorIdUseCase buscarMedicoPorIdUseCase;
    @MockitoBean private AtualizarMedicoUseCase atualizarMedicoUseCase;
    @MockitoBean private InativarMedicoUseCase inativarMedicoUseCase;

    // 👇 Adicione estes dois mocks para o Spring Security parar de dar erro nos testes!
    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private com.natalfy.infrastructure.security.TokenService tokenService;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private com.natalfy.domain.repository.UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve retornar 201 Created ao cadastrar médico válido")
    void deveRetornar201AoCadastrarMedico() throws Exception {
        MedicoRequestDTO request = new MedicoRequestDTO("Dr. Carlos", "123456", "Obstetrícia", "carlos@email.com", "11999999999");
        MedicoResponseDTO response = new MedicoResponseDTO(UUID.randomUUID(), "Dr. Carlos", "123456", "Obstetrícia", "carlos@email.com");

        when(cadastrarMedicoUseCase.executar(any(MedicoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Dr. Carlos"))
                .andExpect(jsonPath("$.crm").value("123456"));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao enviar DTO sem nome (Testando a validação do DTO!)")
    void deveRetornar400AoEnviarDtoInvalido() throws Exception {
        // Criando um DTO com o nome EM BRANCO (Isso vai ativar o @NotBlank do DTO)
        MedicoRequestDTO requestInvalido = new MedicoRequestDTO("", "123456", "Obstetrícia", "carlos@email.com", "11999999999");

        mockMvc.perform(post("/api/v1/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest()); // Prova que o DTO barrou a entrada!
    }
}