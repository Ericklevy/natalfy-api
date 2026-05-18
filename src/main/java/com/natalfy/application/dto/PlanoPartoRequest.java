package com.natalfy.application.dto;

public record PlanoPartoRequest(
        String nomeGestante,
        String condicoesMedicas,
        String preferencias
) {}