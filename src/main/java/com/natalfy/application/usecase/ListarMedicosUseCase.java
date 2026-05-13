package com.natalfy.application.usecase;

import com.natalfy.application.dto.MedicoResponseDTO;
import com.natalfy.domain.repository.MedicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarMedicosUseCase {

    private final MedicoRepository medicoRepository;

    public ListarMedicosUseCase(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    public List<MedicoResponseDTO> executar() {
        return medicoRepository.findAll()
                .stream()
                .map(medico -> new MedicoResponseDTO(
                        medico.getId(),
                        medico.getNome(),
                        medico.getCrm(),
                        medico.getEspecialidade(),
                        medico.getEmail()
                ))
                .collect(Collectors.toList());
    }
}