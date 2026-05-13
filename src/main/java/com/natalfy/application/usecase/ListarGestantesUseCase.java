package com.natalfy.application.usecase;

import com.natalfy.application.dto.GestanteResponseDTO;
import com.natalfy.domain.repository.GestanteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarGestantesUseCase {

    private final GestanteRepository gestanteRepository;

    public ListarGestantesUseCase(GestanteRepository gestanteRepository) {
        this.gestanteRepository = gestanteRepository;
    }

    public List<GestanteResponseDTO> executar() {
        // 1. Busca todo mundo no banco de dados
        // 2. Transforma cada "Gestante" (Modelo) em um "GestanteResponseDTO" (Bandeja)
        // 3. Devolve a lista pronta e segura!
        return gestanteRepository.findAll().stream()
                .map(gestante -> new GestanteResponseDTO(
                        gestante.getId(),
                        gestante.getNome(),
                        gestante.getEmail(),
                        gestante.getDataCadastro()
                ))
                .collect(Collectors.toList());
    }
}