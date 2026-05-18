package com.natalfy.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.natalfy.application.dto.ConsultaRequestDTO;
import com.natalfy.application.dto.ConsultaResponseDTO;
import com.natalfy.application.dto.MotivoCancelamentoDTO;
import com.natalfy.application.usecase.AgendarConsultaUseCase;
import com.natalfy.application.usecase.CancelarConsultaUseCase;
import com.natalfy.application.usecase.FinalizarConsultaUseCase;
import com.natalfy.application.usecase.ListarConsultasUseCase;
import com.natalfy.domain.model.StatusConsulta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ConsultaController.class)
@AutoConfigureMockMvc(addFilters = false)
class ConsultaControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private AgendarConsultaUseCase agendarConsultaUseCase;
    @MockitoBean private ListarConsultasUseCase listarConsultasUseCase;
    @MockitoBean private CancelarConsultaUseCase cancelarConsultaUseCase;
    @MockitoBean private FinalizarConsultaUseCase finalizarConsultaUseCase;

    @MockitoBean
    private com.natalfy.infrastructure.security.TokenService tokenService;

    @MockitoBean
    private com.natalfy.domain.repository.UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Deve retornar 201 Created ao agendar consulta")
    void deveRetornar201AoAgendar() throws Exception {
        ConsultaRequestDTO request = new ConsultaRequestDTO(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now().plusDays(2));
        ConsultaResponseDTO response = new ConsultaResponseDTO(UUID.randomUUID(), request.medicoId(), "Dr. Carlos", request.gestanteId(), "Maria", request.dataHora(), StatusConsulta.AGENDADA);

        when(agendarConsultaUseCase.executar(any(ConsultaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("AGENDADA"));
    }

    @Test
    @DisplayName("Deve barrar requisição 400 Bad Request se a data for no passado")
    void deveRetornar400QuandoDataNoPassado() throws Exception {
        LocalDateTime dataNoPassado = LocalDateTime.now().minusDays(1);
        ConsultaRequestDTO requestInvalido = new ConsultaRequestDTO(UUID.randomUUID(), UUID.randomUUID(), dataNoPassado);

        mockMvc.perform(post("/api/v1/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Não é possível agendar uma consulta no passado"));

        verifyNoInteractions(agendarConsultaUseCase);
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao cancelar consulta")
    void deveRetornar200AoCancelar() throws Exception {
        UUID idConsulta = UUID.randomUUID();
        MotivoCancelamentoDTO request = new MotivoCancelamentoDTO("Paciente desistiu");
        ConsultaResponseDTO response = new ConsultaResponseDTO(idConsulta, UUID.randomUUID(), "Dr. Carlos", UUID.randomUUID(), "Maria", LocalDateTime.now(), StatusConsulta.CANCELADA);

        when(cancelarConsultaUseCase.executar(eq(idConsulta), any(MotivoCancelamentoDTO.class))).thenReturn(response);

        mockMvc.perform(patch("/api/v1/consultas/" + idConsulta + "/cancelar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELADA"));
    }
}