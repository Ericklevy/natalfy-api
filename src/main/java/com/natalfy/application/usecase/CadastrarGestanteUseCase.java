package com.natalfy.application.usecase;

import com.natalfy.application.dto.GestanteRequestDTO;
import com.natalfy.application.dto.GestanteResponseDTO;
import com.natalfy.domain.model.Gestante;
import com.natalfy.domain.repository.GestanteRepository;
import org.springframework.stereotype.Service;

@Service
public class CadastrarGestanteUseCase {

    private final GestanteRepository gestanteRepository;

    public CadastrarGestanteUseCase(GestanteRepository gestanteRepository) {
        this.gestanteRepository = gestanteRepository;
    }

    public GestanteResponseDTO executar(GestanteRequestDTO request) {

        // 1. Regras de Negócio
        if (gestanteRepository.findByCpf(request.cpf()).isPresent()) {
            throw new IllegalArgumentException("Já existe uma gestante cadastrada com este CPF.");
        }

        if (gestanteRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Já existe uma gestante cadastrada com este E-mail.");
        }

        // 2. Converte usando o Padrão Builder (O objeto nasce completo e válido)
        Gestante novaGestante = Gestante.builder()
                .nome(request.nome())
                .cpf(request.cpf())
                .dataNascimento(request.dataNascimento())
                .email(request.email())
                .telefone(request.telefone())
                .build();

        // 3. Salva no banco
        Gestante gestanteSalva = gestanteRepository.save(novaGestante);

        // 4. Devolve a resposta
        return new GestanteResponseDTO(
                gestanteSalva.getId(),
                gestanteSalva.getNome(),
                gestanteSalva.getEmail(),
                gestanteSalva.getDataCadastro()
        );
    }
}