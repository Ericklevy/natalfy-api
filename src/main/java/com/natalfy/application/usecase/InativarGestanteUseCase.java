package com.natalfy.application.usecase;

import com.natalfy.domain.exception.GestanteNaoEncontradaException;
import com.natalfy.domain.model.Gestante;
import com.natalfy.domain.repository.GestanteRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InativarGestanteUseCase {

    private final GestanteRepository gestanteRepository;

    public InativarGestanteUseCase(GestanteRepository gestanteRepository) {
        this.gestanteRepository = gestanteRepository;
    }

    public void executar(UUID id) {
        // 1. Busca a paciente no banco (ou lança erro 404 se não achar)
        Gestante gestante = gestanteRepository.findById(id)
                .orElseThrow(() -> new GestanteNaoEncontradaException("Gestante não encontrada para exclusão."));

        // 2. Aperta o botão de desliga da entidade
        gestante.inativar();

        // 3. Salva a mudança no banco de dados
        gestanteRepository.save(gestante);
    }
}