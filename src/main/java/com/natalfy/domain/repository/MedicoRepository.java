package com.natalfy.domain.repository;

import com.natalfy.domain.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MedicoRepository extends JpaRepository<Medico, UUID> {

    // O Spring faz a mágica de criar o SQL de busca (SELECT) apenas lendo o nome do método!
    boolean existsByCrm(String crm);

    boolean existsByEmail(String email);


    Long countByAtivoTrue();
}