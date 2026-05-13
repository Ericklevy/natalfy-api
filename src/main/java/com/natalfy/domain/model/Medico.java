package com.natalfy.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tb_medicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 20)
    private String crm; // O CRM substitui o CPF como documento de validação principal

    @Column(nullable = false, length = 50)
    private String especialidade; // Ex: "Obstetrícia", "Ginecologia"

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Builder.Default
    private boolean ativo = true;

    // Regra de Negócio DDD: O Médico não pode mudar o próprio CRM nem a Especialidade
    public void atualizarDados(String nome, String email, String telefone) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public void inativar() {
        this.ativo = false;
    }
}