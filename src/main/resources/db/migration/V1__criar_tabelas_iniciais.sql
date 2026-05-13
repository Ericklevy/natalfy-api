-- V1: Criação das tabelas iniciais da Clínica Natalfy

CREATE TABLE tb_gestantes (
                              id UUID PRIMARY KEY,
                              ativo BOOLEAN NOT NULL,
                              cpf VARCHAR(14) NOT NULL UNIQUE,
                              data_cadastro DATE NOT NULL,
                              data_nascimento DATE NOT NULL,
                              email VARCHAR(150) NOT NULL UNIQUE,
                              nome VARCHAR(100) NOT NULL,
                              telefone VARCHAR(20)
);

CREATE TABLE tb_medicos (
                            id UUID PRIMARY KEY,
                            ativo BOOLEAN NOT NULL,
                            crm VARCHAR(20) NOT NULL UNIQUE,
                            email VARCHAR(150) NOT NULL UNIQUE,
                            especialidade VARCHAR(50) NOT NULL,
                            nome VARCHAR(100) NOT NULL,
                            telefone VARCHAR(20)
);

CREATE TABLE tb_usuarios (
                             id UUID PRIMARY KEY,
                             login VARCHAR(255) NOT NULL UNIQUE,
                             role VARCHAR(255) NOT NULL CHECK (role IN ('ADMIN','USER')),
                             senha VARCHAR(255) NOT NULL
);

CREATE TABLE tb_consultas (
                              id UUID PRIMARY KEY,
                              data_hora TIMESTAMP(6) NOT NULL,
                              motivo_cancelamento VARCHAR(255),
                              prontuario TEXT,
                              status VARCHAR(20) NOT NULL CHECK (status IN ('AGENDADA','CANCELADA','REALIZADA')),
                              gestante_id UUID NOT NULL,
                              medico_id UUID NOT NULL,
                              CONSTRAINT fk_gestante FOREIGN KEY (gestante_id) REFERENCES tb_gestantes (id),
                              CONSTRAINT fk_medico FOREIGN KEY (medico_id) REFERENCES tb_medicos (id)
);