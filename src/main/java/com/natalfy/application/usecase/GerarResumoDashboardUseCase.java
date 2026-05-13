package com.natalfy.application.usecase;

import com.natalfy.application.dto.ResumoDashboardDTO;
import com.natalfy.domain.repository.ConsultaRepository;
import com.natalfy.domain.repository.GestanteRepository;
import com.natalfy.domain.repository.MedicoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class GerarResumoDashboardUseCase {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final GestanteRepository gestanteRepository;

    public GerarResumoDashboardUseCase(ConsultaRepository consultaRepository, MedicoRepository medicoRepository, GestanteRepository gestanteRepository) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.gestanteRepository = gestanteRepository;
    }

    public ResumoDashboardDTO executar() {
        // Pega o dia de hoje, da meia-noite até às 23:59:59
        LocalDateTime inicioDoDia = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime fimDoDia = LocalDateTime.now().with(LocalTime.MAX);

        Long consultasHoje = consultaRepository.contarConsultasDoDia(inicioDoDia, fimDoDia);
        Long medicosAtivos = medicoRepository.countByAtivoTrue();
        Long totalGestantes = gestanteRepository.count();

        return new ResumoDashboardDTO(consultasHoje, medicosAtivos, totalGestantes);
    }
}