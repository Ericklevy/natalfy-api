package com.natalfy.application.usecase;

import com.natalfy.application.dto.ConsultaRequestDTO;
import com.natalfy.application.dto.ConsultaResponseDTO;
import com.natalfy.domain.event.ConsultaAgendadaEvent; // 1. Importe o Evento
import com.natalfy.domain.exception.GestanteNaoEncontradaException;
import com.natalfy.domain.exception.MedicoNaoEncontradoException;
import com.natalfy.domain.model.Consulta;
import com.natalfy.domain.model.Gestante;
import com.natalfy.domain.model.Medico;
import com.natalfy.domain.repository.ConsultaRepository;
import com.natalfy.domain.repository.GestanteRepository;
import com.natalfy.domain.repository.MedicoRepository;
import com.natalfy.domain.validator.ValidadorAgendamentoConsulta;
import org.springframework.context.ApplicationEventPublisher; // 2. Importe o Publisher
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendarConsultaUseCase {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final GestanteRepository gestanteRepository;
    private final List<ValidadorAgendamentoConsulta> validadores;

    // 👇 Injetando o megafone do Spring
    private final ApplicationEventPublisher eventPublisher;

    public AgendarConsultaUseCase(ConsultaRepository consultaRepository,
                                  MedicoRepository medicoRepository,
                                  GestanteRepository gestanteRepository,
                                  List<ValidadorAgendamentoConsulta> validadores,
                                  ApplicationEventPublisher eventPublisher) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.gestanteRepository = gestanteRepository;
        this.validadores = validadores;
        this.eventPublisher = eventPublisher; // Inicializa o megafone
    }

    public ConsultaResponseDTO executar(ConsultaRequestDTO request) {

        validadores.forEach(v -> v.validar(request));

        Medico medico = medicoRepository.findById(request.medicoId())
                .orElseThrow(() -> new MedicoNaoEncontradoException("Médico não encontrado."));

        if (!medico.isAtivo()) {
            throw new IllegalArgumentException("Consulta não pode ser agendada com um médico inativo.");
        }

        Gestante gestante = gestanteRepository.findById(request.gestanteId())
                .orElseThrow(() -> new GestanteNaoEncontradaException("Gestante não encontrada."));

        if (!gestante.isAtivo()) {
            throw new IllegalArgumentException("Consulta não pode ser agendada para uma gestante inativa.");
        }

        boolean jaExisteConsulta = consultaRepository.existsByMedicoIdAndDataHora(medico.getId(), request.dataHora());
        if (jaExisteConsulta) {
            throw new IllegalArgumentException("O médico já possui uma consulta agendada para este horário exato.");
        }

        Consulta consulta = Consulta.builder()
                .medico(medico)
                .gestante(gestante)
                .dataHora(request.dataHora())
                .build();

        consultaRepository.save(consulta);

        // 👇 O GRITO: Publica o evento logo após salvar no banco!
        eventPublisher.publishEvent(new ConsultaAgendadaEvent(
                consulta.getId(),
                gestante.getNome(),
                medico.getNome(),
                consulta.getDataHora()
        ));

        return new ConsultaResponseDTO(
                consulta.getId(), medico.getId(), medico.getNome(),
                gestante.getId(), gestante.getNome(), consulta.getDataHora(), consulta.getStatus()
        );
    }
}