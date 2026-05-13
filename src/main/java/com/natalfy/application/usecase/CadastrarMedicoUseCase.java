package com.natalfy.application.usecase;

import com.natalfy.application.dto.MedicoRequestDTO;
import com.natalfy.application.dto.MedicoResponseDTO;
import com.natalfy.domain.model.Medico;
import com.natalfy.domain.repository.MedicoRepository;
import org.springframework.stereotype.Service;

@Service
public class CadastrarMedicoUseCase {

    private final MedicoRepository medicoRepository;

    public CadastrarMedicoUseCase(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    public MedicoResponseDTO executar(MedicoRequestDTO request) {
        // 1. Validação de Regra de Negócio: CRM e E-mail únicos
        if (medicoRepository.existsByCrm(request.crm())) {
            throw new IllegalArgumentException("Já existe um médico cadastrado com este CRM.");
        }

        if (medicoRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Já existe um médico cadastrado com este E-mail.");
        }

        // 2. Transforma o DTO em Entidade (Padrão Builder)
        Medico medico = Medico.builder()
                .nome(request.nome())
                .crm(request.crm())
                .especialidade(request.especialidade())
                .email(request.email())
                .telefone(request.telefone())
                .build();

        // 3. Salva no banco de dados
        medicoRepository.save(medico);

        // 4. Transforma a Entidade no DTO de Saída
        return new MedicoResponseDTO(
                medico.getId(),
                medico.getNome(),
                medico.getCrm(),
                medico.getEspecialidade(),
                medico.getEmail()
        );
    }
}