// ListarGestantesUseCase.java

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
        return gestanteRepository.findAllByAtivoTrue().stream()
                .map(gestante -> new GestanteResponseDTO(
                        gestante.getId(),
                        gestante.getNome(),
                        gestante.getEmail(),
                        gestante.getDataCadastro()
                ))
                .collect(Collectors.toList());
    }
}