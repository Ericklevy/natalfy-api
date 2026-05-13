package com.natalfy.application.usecase;

import com.natalfy.application.dto.ConsultaResponseDTO;
import com.natalfy.application.dto.MotivoCancelamentoDTO;
import com.natalfy.domain.model.Consulta;
import com.natalfy.domain.model.StatusConsulta;
import com.natalfy.domain.repository.ConsultaRepository;
import com.natalfy.domain.validator.ValidadorCancelamentoConsulta;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CancelarConsultaUseCase {

    private final ConsultaRepository repository;
    private final List<ValidadorCancelamentoConsulta> validadores; // Injetando os validadores!

    public CancelarConsultaUseCase(ConsultaRepository repository, List<ValidadorCancelamentoConsulta> validadores) {
        this.repository = repository;
        this.validadores = validadores;
    }

    public ConsultaResponseDTO executar(UUID idConsulta, MotivoCancelamentoDTO motivoDTO) {
        Consulta consulta = repository.findById(idConsulta)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada."));

        if (consulta.getStatus() == StatusConsulta.CANCELADA) {
            throw new IllegalArgumentException("Esta consulta já está cancelada.");
        }

        // MÁGICA AQUI: Roda todas as regras de cancelamento
        validadores.forEach(v -> v.validar(consulta));

        consulta.setStatus(StatusConsulta.CANCELADA);
        consulta.setMotivoCancelamento(motivoDTO.motivo()); // Lembre-se de ter esse campo na Entidade Consulta!

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