package com.natalfy.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_consultas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // 👇 Relacionamento: Muitas consultas podem pertencer a uma Gestante
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gestante_id", nullable = false)
    private Gestante gestante;

    // 👇 Relacionamento: Muitas consultas podem pertencer a um Médico
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatusConsulta status = StatusConsulta.AGENDADA;

    @Column(length = 255)
    private String motivoCancelamento;

    @Column(columnDefinition = "TEXT")
    private String prontuario; // O texto livre do médico sobre o atendimento

    // Regras de Negócio DDD
    public void cancelar(String motivo) {
        this.status = StatusConsulta.CANCELADA;
        this.motivoCancelamento = motivo;
    }

    public void marcarComoRealizada(String prontuarioMedica) {
        this.status = StatusConsulta.REALIZADA;
        this.prontuario = prontuarioMedica;
    }
}