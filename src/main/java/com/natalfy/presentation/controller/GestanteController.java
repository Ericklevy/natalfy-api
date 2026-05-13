package com.natalfy.presentation.controller;

import com.natalfy.application.dto.GestanteRequestDTO;
import com.natalfy.application.dto.GestanteResponseDTO;
import com.natalfy.application.dto.GestanteUpdateRequestDTO; // <-- Import novo
import com.natalfy.application.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/gestantes")
@Tag(name = "Gestantes", description = "Endpoints para gerenciamento de gestantes")
public class GestanteController {

    private final CadastrarGestanteUseCase cadastrarGestanteUseCase;
    private final ListarGestantesUseCase listarGestantesUseCase;
    private final BuscarGestantePorIdUseCase buscarGestantePorIdUseCase;
    private final AtualizarGestanteUseCase atualizarGestanteUseCase; // <-- NOVO
    private final InativarGestanteUseCase inativarGestanteUseCase; // <-- Adicione isto

    public GestanteController(
            CadastrarGestanteUseCase cadastrarGestanteUseCase,
            ListarGestantesUseCase listarGestantesUseCase,
            BuscarGestantePorIdUseCase buscarGestantePorIdUseCase,
            AtualizarGestanteUseCase atualizarGestanteUseCase, InativarGestanteUseCase inativarGestanteUseCase) { // <-- NOVO
        this.cadastrarGestanteUseCase = cadastrarGestanteUseCase;
        this.listarGestantesUseCase = listarGestantesUseCase;
        this.buscarGestantePorIdUseCase = buscarGestantePorIdUseCase;
        this.atualizarGestanteUseCase = atualizarGestanteUseCase; // <-- NOVO
        this.inativarGestanteUseCase = inativarGestanteUseCase;
    }

    @PostMapping
    @Operation(summary = "Cadastrar Gestante", description = "Cadastra uma nova gestante no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Gestante cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada (ex: CPF já cadastrado)")
    })
    public ResponseEntity<GestanteResponseDTO> cadastrar(@RequestBody @Valid GestanteRequestDTO request) {
        GestanteResponseDTO response = cadastrarGestanteUseCase.executar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar Gestantes", description = "Retorna uma lista de todas as gestantes ativas cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem retornada com sucesso")
    })
    public ResponseEntity<List<GestanteResponseDTO>> listarTodas() {
        List<GestanteResponseDTO> response = listarGestantesUseCase.executar();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Gestante por ID", description = "Retorna os dados de uma gestante específica pelo seu identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gestante encontrada"),
            @ApiResponse(responseCode = "404", description = "Gestante não encontrada")
    })
    public ResponseEntity<GestanteResponseDTO> buscarPorId(@PathVariable UUID id) {
        GestanteResponseDTO response = buscarGestantePorIdUseCase.executar(id);
        return ResponseEntity.ok(response);
    }

    // 👇 NOVA ROTA DE ATUALIZAÇÃO ADICIONADA 👇
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Gestante", description = "Atualiza os dados de uma gestante existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gestante atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados"),
            @ApiResponse(responseCode = "404", description = "Gestante não encontrada"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada (ex: e-mail já em uso)")
    })
    public ResponseEntity<GestanteResponseDTO> atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid GestanteUpdateRequestDTO request) {

        GestanteResponseDTO response = atualizarGestanteUseCase.executar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Inativar Gestante", description = "Inativa (exclusão lógica) uma gestante no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Gestante inativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Gestante não encontrada")
    })
    public ResponseEntity<Void> inativar(@PathVariable UUID id) {
        inativarGestanteUseCase.executar(id);
        // Retorna HTTP 204 (No Content) indicando que a ação foi um sucesso e não há dados para retornar.
        return ResponseEntity.noContent().build();
    }
}