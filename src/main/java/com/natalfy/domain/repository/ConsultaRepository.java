package com.natalfy.domain.repository;

import com.natalfy.domain.model.Consulta;
import com.natalfy.domain.model.StatusConsulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ConsultaRepository extends JpaRepository<Consulta, UUID> {

    boolean existsByMedicoIdAndDataHora(UUID medicoId, LocalDateTime dataHora);


    // 👇 NOVA QUERY DE FILTRO E PAGINAÇÃO 👇
    @Query("SELECT c FROM Consulta c WHERE " +
            "(:medicoId IS NULL OR c.medico.id = :medicoId) AND " +
            "(:status IS NULL OR c.status = :status)")
    Page<Consulta> buscarComFiltros(
            @Param("medicoId") UUID medicoId,
            @Param("status") StatusConsulta status,
            Pageable pageable);


    // 👇 Fica mais limpo usando apenas @Query e LocalDateTime já importados
    @Query("SELECT COUNT(c) FROM Consulta c WHERE c.dataHora >= :inicio AND c.dataHora <= :fim")
    Long contarConsultasDoDia(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}