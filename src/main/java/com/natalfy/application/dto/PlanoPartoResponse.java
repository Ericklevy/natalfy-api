package com.natalfy.application.dto; // Ajuste o pacote conforme sua estrutura

import java.time.LocalDateTime;

public record PlanoPartoResponse(
        String nomeGestante,
        String statusClinico,
        String planoGerado,
        LocalDateTime geradoEm
) {}