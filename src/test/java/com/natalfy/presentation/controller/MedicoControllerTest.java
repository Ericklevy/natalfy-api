package com.natalfy.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.natalfy.application.dto.MedicoRequestDTO;
import com.natalfy.application.dto.MedicoResponseDTO;
import com.natalfy.application.usecase.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
@AutoConfigureMockMvc(addFilters = false)
class MedicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean private CadastrarMedicoUseCase cadastrarMedicoUseCase;
    @MockitoBean private ListarMedicosUseCase listarMedicosUseCase;
    @MockitoBean private BuscarMedicoPorIdUseCase buscarMedicoPorIdUseCase;
    @MockitoBean private AtualizarMedicoUseCase atualizarMedicoUseCase;
    @MockitoBean private InativarMedicoUseCase inativarMedicoUseCase;

    @MockitoBean
    private com.natalfy.infrastructure.security.TokenService tokenService;

    @MockitoBean
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
    @DisplayName("Deve retornar 400 Bad Request ao enviar DTO sem nome")
    void deveRetornar400AoEnviarDtoInvalido() throws Exception {
        MedicoRequestDTO requestInvalido = new MedicoRequestDTO("", "123456", "Obstetrícia", "carlos@email.com", "11999999999");

        mockMvc.perform(post("/api/v1/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }
}