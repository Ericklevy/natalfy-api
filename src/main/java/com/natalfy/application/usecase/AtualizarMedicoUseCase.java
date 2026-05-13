package com.natalfy.application.usecase;

import com.natalfy.application.dto.MedicoResponseDTO;
import com.natalfy.application.dto.MedicoUpdateRequestDTO;
import com.natalfy.domain.exception.MedicoNaoEncontradoException;
import com.natalfy.domain.model.Medico;
import com.natalfy.domain.repository.MedicoRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AtualizarMedicoUseCase {
    private final MedicoRepository repository;
    public AtualizarMedicoUseCase(MedicoRepository repository) { this.repository = repository; }

    public MedicoResponseDTO executar(UUID id, MedicoUpdateRequestDTO request) {
        Medico medico = repository.findById(id).orElseThrow(() -> new MedicoNaoEncontradoException("Médico não encontrado."));
        medico.atualizarDados(request.nome(), request.email(), request.telefone());
        repository.save(medico);
        return new MedicoResponseDTO(medico.getId(), medico.getNome(), medico.getCrm(), medico.getEspecialidade(), medico.getEmail());
    }
}