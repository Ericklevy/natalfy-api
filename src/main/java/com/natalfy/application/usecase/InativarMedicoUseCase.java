package com.natalfy.application.usecase;

import com.natalfy.domain.exception.MedicoNaoEncontradoException;
import com.natalfy.domain.model.Medico;
import com.natalfy.domain.repository.MedicoRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class InativarMedicoUseCase {
    private final MedicoRepository repository;
    public InativarMedicoUseCase(MedicoRepository repository) { this.repository = repository; }

    public void executar(UUID id) {
        Medico medico = repository.findById(id).orElseThrow(() -> new MedicoNaoEncontradoException("Médico não encontrado."));
        medico.inativar();
        repository.save(medico);
    }
}