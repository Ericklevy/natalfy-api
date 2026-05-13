package com.natalfy.application.usecase;

import com.natalfy.application.dto.GestanteResponseDTO;
import com.natalfy.application.dto.GestanteUpdateRequestDTO;
import com.natalfy.domain.exception.GestanteNaoEncontradaException;
import com.natalfy.domain.model.Gestante;
import com.natalfy.domain.repository.GestanteRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AtualizarGestanteUseCase {

    private final GestanteRepository gestanteRepository;

    public AtualizarGestanteUseCase(GestanteRepository gestanteRepository) {
        this.gestanteRepository = gestanteRepository;
    }

    public GestanteResponseDTO executar(UUID id, GestanteUpdateRequestDTO request) {
        // 1. Busca a paciente no banco (ou lança erro 404)
        Gestante gestante = gestanteRepository.findById(id)
                .orElseThrow(() -> new GestanteNaoEncontradaException("Gestante não encontrada para atualização."));

        // 2. A própria entidade se atualiza com os dados que vieram do DTO
        gestante.atualizarDados(request.nome(), request.dataNascimento(), request.email(), request.telefone());

        // 3. Salva no banco de dados
        gestanteRepository.save(gestante);

        // 4. Devolve os dados atualizados para quem chamou
        return new GestanteResponseDTO(
                gestante.getId(),
                gestante.getNome(),
                gestante.getEmail(),
                gestante.getDataCadastro()
        );
    }
}