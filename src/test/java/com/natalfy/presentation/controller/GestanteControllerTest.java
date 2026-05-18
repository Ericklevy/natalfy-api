package com.natalfy.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.natalfy.application.dto.GestanteRequestDTO;
import com.natalfy.application.dto.GestanteResponseDTO;
import com.natalfy.application.usecase.CadastrarGestanteUseCase;
import com.natalfy.application.usecase.ListarGestantesUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// ✅ Pacotes novos do Spring Boot 4
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GestanteController.class)
@AutoConfigureMockMvc(addFilters = false)
class GestanteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CadastrarGestanteUseCase cadastrarUseCase; // (Mudei o nome desse para não confundir)

    @MockitoBean
    private ListarGestantesUseCase listarUseCase; // <-- Adicionei esse aqui!

    // 👇 ADICIONE ESTES 3 AQUI 👇
    @MockitoBean
    private com.natalfy.application.usecase.BuscarGestantePorIdUseCase buscarPorIdUseCase;

    @MockitoBean
    private com.natalfy.application.usecase.AtualizarGestanteUseCase atualizarUseCase;

    @MockitoBean
    private com.natalfy.application.usecase.InativarGestanteUseCase inativarUseCase;

    // 👇 Adicione estes dois mocks para o Spring Security parar de dar erro nos testes!
    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private com.natalfy.infrastructure.security.TokenService tokenService;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private com.natalfy.domain.repository.UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve retornar HTTP 201 (Created) e o JSON da gestante quando os dados forem válidos")
    void deveRetornar201QuandoDadosValidos() throws Exception {
        GestanteRequestDTO request = new GestanteRequestDTO(
                "Amanda Silva", "52998224725", LocalDate.of(1995, 5, 10), "amanda@email.com", "61999999999"
        );

        GestanteResponseDTO responseSimulada = new GestanteResponseDTO(
                UUID.randomUUID(), "Amanda Silva", "amanda@email.com", LocalDate.now()
        );

        when(cadastrarUseCase.executar(any(GestanteRequestDTO.class))).thenReturn(responseSimulada);

        mockMvc.perform(post("/api/v1/gestantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Amanda Silva"));
    }

    @Test
    @DisplayName("Deve retornar HTTP 400 (Bad Request) e o JSON de erro se o CPF for inválido")
    void deveRetornar400QuandoCpfInvalido() throws Exception {
        GestanteRequestDTO requestComCpfRuim = new GestanteRequestDTO(
                "Amanda Silva", "12345678900", LocalDate.of(1995, 5, 10), "amanda@email.com", "61999999999"
        );

        mockMvc.perform(post("/api/v1/gestantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestComCpfRuim)))

                .andExpect(status().isBadRequest())
                // 👇 ADICIONE ESTAS 3 LINHAS NOVAS 👇
                .andExpect(jsonPath("$.message").value("Erro de validação nos dados enviados."))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value("O CPF informado é inválido"));

        verifyNoInteractions(cadastrarUseCase);
    }

    @Test
    @DisplayName("Deve retornar HTTP 200 (OK) e a lista de gestantes ao fazer um GET")
    void deveRetornar200EListarGestantes() throws Exception {
        GestanteResponseDTO g1 = new GestanteResponseDTO(UUID.randomUUID(), "Ana Paula", "ana@email.com", LocalDate.now());

        when(listarUseCase.executar()).thenReturn(List.of(g1));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/v1/gestantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()) // Confere se a resposta é um Array [ ]
                .andExpect(jsonPath("$[0].nome").value("Ana Paula"));
    }
}
