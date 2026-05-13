package com.natalfy.domain.repository;

import com.natalfy.domain.model.Gestante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GestanteRepository extends JpaRepository<Gestante, UUID> {
    Optional<Gestante> findByCpf(String cpf);
    Optional<Gestante> findByEmail(String email);
}