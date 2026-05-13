package com.natalfy.presentation.controller;

import com.natalfy.application.dto.ConsultaRequestDTO;
import com.natalfy.application.dto.ConsultaResponseDTO;
import com.natalfy.application.dto.MotivoCancelamentoDTO;
import com.natalfy.application.dto.ProntuarioRequestDTO;
import com.natalfy.application.usecase.AgendarConsultaUseCase;
import com.natalfy.application.usecase.CancelarConsultaUseCase;
import com.natalfy.application.usecase.FinalizarConsultaUseCase;
import com.natalfy.application.usecase.ListarConsultasUseCase;
import com.natalfy.domain.model.StatusConsulta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/consultas")
@Tag(name = "Consultas", description = "Endpoints para gerenciamento de consultas médicas")
public class ConsultaController {

    private final AgendarConsultaUseCase agendarConsultaUseCase;
    private final ListarConsultasUseCase listarConsultasUseCase;
    private final CancelarConsultaUseCase cancelarConsultaUseCase;
    private final FinalizarConsultaUseCase finalizarConsultaUseCase;

    public ConsultaController(AgendarConsultaUseCase agendarConsultaUseCase,
                              ListarConsultasUseCase listarConsultasUseCase,
                              CancelarConsultaUseCase cancelarConsultaUseCase,
                              FinalizarConsultaUseCase finalizarConsultaUseCase) {
        this.agendarConsultaUseCase = agendarConsultaUseCase;
        this.listarConsultasUseCase = listarConsultasUseCase;
        this.cancelarConsultaUseCase = cancelarConsultaUseCase;
        this.finalizarConsultaUseCase = finalizarConsultaUseCase;
    }

    @PostMapping
    @Operation(summary = "Agendar Consulta", description = "Agenda uma nova consulta para uma gestante com um médico em uma data e hora específicas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Consulta agendada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada (ex: médico já ocupado no horário)"),
            @ApiResponse(responseCode = "404", description = "Médico ou gestante não encontrados")
    })
    public ResponseEntity<ConsultaResponseDTO> agendar(@RequestBody @Valid ConsultaRequestDTO request) {
        ConsultaResponseDTO response = agendarConsultaUseCase.executar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar Consultas", description = "Retorna uma lista paginada de consultas agendadas, com filtros opcionais por médico ou status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem retornada com sucesso")
    })
    public ResponseEntity<Page<ConsultaResponseDTO>> listarTodas(
            @RequestParam(required = false) UUID medicoId,
            @RequestParam(required = false) StatusConsulta status,
            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(listarConsultasUseCase.executar(medicoId, status, pageable));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar Consulta", description = "Cancela uma consulta informando um motivo de cancelamento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta cancelada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação no motivo de cancelamento"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada (ex: consulta já finalizada)")
    })
    public ResponseEntity<ConsultaResponseDTO> cancelar(@PathVariable UUID id, @RequestBody @Valid MotivoCancelamentoDTO request) {
        ConsultaResponseDTO response = cancelarConsultaUseCase.executar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/finalizar")
    @Operation(summary = "Finalizar Consulta", description = "Finaliza uma consulta informando o prontuário com as anotações do médico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta finalizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação no prontuário"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada (ex: consulta cancelada não pode ser finalizada)")
    })
    public ResponseEntity<ConsultaResponseDTO> finalizar(@PathVariable UUID id, @RequestBody @Valid ProntuarioRequestDTO request) {
        ConsultaResponseDTO response = finalizarConsultaUseCase.executar(id, request);
        return ResponseEntity.ok(response);
    }
}