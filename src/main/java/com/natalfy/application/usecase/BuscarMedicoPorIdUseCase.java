package com.natalfy.application.usecase;

import com.natalfy.application.dto.MedicoResponseDTO;
import com.natalfy.domain.exception.MedicoNaoEncontradoException;
import com.natalfy.domain.model.Medico;
import com.natalfy.domain.repository.MedicoRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class BuscarMedicoPorIdUseCase {
    private final MedicoRepository repository;
    public BuscarMedicoPorIdUseCase(MedicoRepository repository) { this.repository = repository; }

    public MedicoResponseDTO executar(UUID id) {
        Medico medico = repository.findById(id).orElseThrow(() -> new MedicoNaoEncontradoException("Médico não encontrado."));
        return new MedicoResponseDTO(medico.getId(), medico.getNome(), medico.getCrm(), medico.getEspecialidade(), medico.getEmail());
    }
}