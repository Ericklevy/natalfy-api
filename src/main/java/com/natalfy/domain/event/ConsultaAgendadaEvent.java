package com.natalfy.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

// Usamos Record porque eventos do passado NUNCA podem ser alterados (são imutáveis)
public record ConsultaAgendadaEvent(
        UUID consultaId,
        String nomeGestante,
        String nomeMedico,
        LocalDateTime dataHora
) {
}