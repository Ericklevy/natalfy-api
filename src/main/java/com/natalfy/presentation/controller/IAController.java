package com.natalfy.presentation.controller;

import com.natalfy.application.dto.PlanoPartoRequest;
import com.natalfy.application.dto.PlanoPartoResponse;
import com.natalfy.application.usecase.GerarPlanoPartoUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ia")
public class IAController {

    private final GerarPlanoPartoUseCase gerarPlanoPartoUseCase;

    public IAController(GerarPlanoPartoUseCase gerarPlanoPartoUseCase) {
        this.gerarPlanoPartoUseCase = gerarPlanoPartoUseCase;
    }

    @PostMapping("/plano-parto")
    public ResponseEntity<PlanoPartoResponse> gerarPlanoParto(@RequestBody PlanoPartoRequest request) {
        // 💡 Ajustado aqui de request.nome() para request.nomeGestante()
        // E concatenando as preferências com as condições médicas para o prompt receber tudo
        String dadosCombinados = "Condições Médicas: " + request.condicoesMedicas() + " | Preferências: " + request.preferencias();

        PlanoPartoResponse response = gerarPlanoPartoUseCase.executar(
                request.nomeGestante(),
                dadosCombinados
        );

        return ResponseEntity.ok(response);
    }
}