package com.natalfy.application.usecase;

import com.natalfy.application.dto.ConsultaResponseDTO;
import com.natalfy.domain.model.StatusConsulta;
import com.natalfy.domain.repository.ConsultaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ListarConsultasUseCase {

    private final ConsultaRepository repository;

    public ListarConsultasUseCase(ConsultaRepository repository) {
        this.repository = repository;
    }

    public Page<ConsultaResponseDTO> executar(UUID medicoId, StatusConsulta status, Pageable pageable) {
        // Usa o nosso novo método do Repository que entende filtros e paginação
        return repository.buscarComFiltros(medicoId, status, pageable)
                .map(consulta -> new ConsultaResponseDTO(
                        consulta.getId(),
                        consulta.getMedico().getId(),
                        consulta.getMedico().getNome(),
                        consulta.getGestante().getId(),
                        consulta.getGestante().getNome(),
                        consulta.getDataHora(),
                        consulta.getStatus()
                ));
    }
}