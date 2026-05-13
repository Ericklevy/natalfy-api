package com.natalfy.application.usecase;

import com.natalfy.application.dto.ConsultaResponseDTO;
import com.natalfy.application.dto.ProntuarioRequestDTO;
import com.natalfy.domain.model.Consulta;
import com.natalfy.domain.model.StatusConsulta;
import com.natalfy.domain.repository.ConsultaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FinalizarConsultaUseCase {

    private final ConsultaRepository repository;

    public FinalizarConsultaUseCase(ConsultaRepository repository) { this.repository = repository; }

    public ConsultaResponseDTO executar(UUID id, ProntuarioRequestDTO dto) {
        Consulta consulta = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada."));

        // Regra de Negócio: Só podemos realizar uma consulta que estava aguardando (AGENDADA)
        if (consulta.getStatus() != StatusConsulta.AGENDADA) {
            throw new IllegalArgumentException("Apenas consultas com status AGENDADA podem ser finalizadas.");
        }

        consulta.marcarComoRealizada(dto.prontuario());
        repository.save(consulta);

        return new ConsultaResponseDTO(
                consulta.getId(),
                consulta.getMedico().getId(),
                consulta.getMedico().getNome(),
                consulta.getGestante().getId(),
                consulta.getGestante().getNome(),
                consulta.getDataHora(),
                consulta.getStatus()
        );
    }
}