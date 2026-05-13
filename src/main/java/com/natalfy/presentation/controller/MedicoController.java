package com.natalfy.presentation.controller;

import com.natalfy.application.dto.MedicoRequestDTO;
import com.natalfy.application.dto.MedicoResponseDTO;
import com.natalfy.application.dto.MedicoUpdateRequestDTO;
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
@RequestMapping("/api/v1/medicos")
@Tag(name = "Médicos", description = "Endpoints para gerenciamento de médicos")
public class MedicoController {

    private final CadastrarMedicoUseCase cadastrar;
    private final ListarMedicosUseCase listar;
    private final BuscarMedicoPorIdUseCase buscarPorId;
    private final AtualizarMedicoUseCase atualizar;
    private final InativarMedicoUseCase inativar;

    public MedicoController(CadastrarMedicoUseCase cadastrar, ListarMedicosUseCase listar, BuscarMedicoPorIdUseCase buscarPorId, AtualizarMedicoUseCase atualizar, InativarMedicoUseCase inativar) {
        this.cadastrar = cadastrar;
        this.listar = listar;
        this.buscarPorId = buscarPorId;
        this.atualizar = atualizar;
        this.inativar = inativar;
    }

    @PostMapping
    @Operation(summary = "Cadastrar Médico", description = "Cadastra um novo médico no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Médico cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada (ex: CRM já cadastrado)")
    })
    public ResponseEntity<MedicoResponseDTO> cadastrar(@RequestBody @Valid MedicoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastrar.executar(request));
    }

    @GetMapping
    @Operation(summary = "Listar Médicos", description = "Retorna uma lista de todos os médicos ativos cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem retornada com sucesso")
    })
    public ResponseEntity<List<MedicoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(listar.executar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Médico por ID", description = "Retorna os dados de um médico específico pelo seu identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médico encontrado"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado")
    })
    public ResponseEntity<MedicoResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(buscarPorId.executar(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Médico", description = "Atualiza os dados de um médico existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médico atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada (ex: e-mail já em uso)")
    })
    public ResponseEntity<MedicoResponseDTO> atualizar(@PathVariable UUID id, @RequestBody @Valid MedicoUpdateRequestDTO request) {
        return ResponseEntity.ok(atualizar.executar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Inativar Médico", description = "Inativa (exclusão lógica) um médico no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Médico inativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado")
    })
    public ResponseEntity<Void> inativar(@PathVariable UUID id) {
        inativar.executar(id);
        return ResponseEntity.noContent().build();
    }
}