package com.natalfy.presentation.controller;

import com.natalfy.application.dto.ResumoDashboardDTO;
import com.natalfy.application.usecase.GerarResumoDashboardUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@Tag(name = "Dashboard", description = "Endpoints para resumo e dados gerais da clínica")
public class DashboardController {

    private final GerarResumoDashboardUseCase gerarResumoDashboardUseCase;

    public DashboardController(GerarResumoDashboardUseCase gerarResumoDashboardUseCase) {
        this.gerarResumoDashboardUseCase = gerarResumoDashboardUseCase;
    }

    @GetMapping
    @Operation(summary = "Obter Resumo", description = "Retorna os dados totalizados para a dashboard (consultas hoje, médicos ativos e total de gestantes).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumo da dashboard obtido com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Token ausente ou inválido)")
    })
    public ResponseEntity<ResumoDashboardDTO> obterResumo() {
        ResumoDashboardDTO resumo = gerarResumoDashboardUseCase.executar();
        return ResponseEntity.ok(resumo);
    }
}