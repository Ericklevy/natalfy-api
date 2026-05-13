package com.natalfy.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_gestantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // <-- O Padrão Builder ativado aqui!
public class Gestante {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(nullable = false, updatable = false)
    private LocalDate dataCadastro;

    @PrePersist
    protected void onCreate() {
        this.dataCadastro = LocalDate.now();
    }


    @Builder.Default
    private boolean ativo = true; // Toda gestante nasce ativa no sistema

    // Adicione este método dentro da classe Gestante
    public void atualizarDados(String nome, LocalDate dataNascimento, String email, String telefone) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.telefone = telefone;
    }

    public void inativar() {
        this.ativo = false;
    }
}