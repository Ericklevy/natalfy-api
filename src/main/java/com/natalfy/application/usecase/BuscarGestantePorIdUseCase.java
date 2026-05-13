package com.natalfy.application.usecase;

import com.natalfy.application.dto.GestanteResponseDTO;
import com.natalfy.domain.exception.GestanteNaoEncontradaException;
import com.natalfy.domain.model.Gestante;
import com.natalfy.domain.repository.GestanteRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarGestantePorIdUseCase {

    private final GestanteRepository gestanteRepository;

    public BuscarGestantePorIdUseCase(GestanteRepository gestanteRepository) {
        this.gestanteRepository = gestanteRepository;
    }

    public GestanteResponseDTO executar(UUID id) {
        Gestante gestante = gestanteRepository.findById(id)
                .orElseThrow(() -> new GestanteNaoEncontradaException("Gestante não encontrada com o ID informado."));

        return new GestanteResponseDTO(
                gestante.getId(),
                gestante.getNome(),
                gestante.getEmail(),
                gestante.getDataCadastro()
        );
    }
}