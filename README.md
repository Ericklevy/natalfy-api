# 🏥 Natalfy API - Gestão de Clínica Obstétrica

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4+-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

A **Natalfy API** é um sistema robusto e moderno de gestão clínica focado no ecossistema de maternidade e pediatria. Desenvolvido para gerenciar o acompanhamento de gestantes, médicos e o agendamento de consultas, o projeto foi concebido seguindo os mais altos padrões de engenharia de software, priorizando escalabilidade, arquitetura limpa, alta testabilidade e segurança da informação.

---

## 🚀 Tecnologias e Ferramentas

O projeto utiliza o estado da arte do ecossistema Java e ferramentas de infraestrutura:

* **Linguagem:** Java 21 (com suporte a Virtual Threads para alta concorrência)
* **Framework:** Spring Boot 3.4.x / 4.0.x
* **Banco de Dados:** PostgreSQL (Persistência relacional segura) e Redis (Cache/Sessão de alta performance)
* **Documentação:** Swagger/OpenAPI 3
* **Segurança:** Spring Security + JWT (JSON Web Token)
* **Migrações:** Flyway Database Migrations
* **Contêineres:** Docker & Docker Compose
* **Inteligência Artificial:** Integração com Google Gemini AI via Spring AI
* **CI/CD:** GitHub Actions (Automação de testes e build)

---

## 🏗️ Arquitetura e Padrões

O sistema foi desenhado utilizando **Clean Architecture** e princípios de **Domain-Driven Design (DDD)**, garantindo que as regras de negócio sejam independentes de frameworks externos:

1. **Domain:** Entidades de negócio, Interfaces de Repositório e Validadores.
2. **Application:** Casos de Uso (Use Cases) e DTOs.
3. **Infrastructure:** Configurações de Segurança, Persistência, Cache e Notificações.
4. **Presentation:** Controllers REST e Handlers de exceção global.

### Diferenciais Técnicos:
* **TDD (Test-Driven Development):**  testes automatizados cobrindo Regras de Negócio, Controllers e DTOs.
* **Blindagem de Infraestrutura:** Uso de Flyway para controle de versão do banco de dados, abandonando a criação automática do Hibernate em produção.

---

## 🛠️ Como Executar o Projeto

### Pré-requisitos:
* Java 21+ instalado.
* Docker e Docker Compose instalados.
* Maven Wrapper (já incluído no projeto).

### Passo a Passo:

1. **Configure as Variáveis de Ambiente:**
   Copie o arquivo `.env.example` para um novo arquivo chamado `.env` na raiz do projeto e preencha com as suas chaves e senhas reais:
   ```bash
   cp .env.example .env
   ```

2. **Gere o artefato da aplicação:**
   ```bash
   ./mvnw clean package -DskipTests
   ```

3. **Suba a infraestrutura completa (API + DB + Redis):**
   ```bash
   docker-compose up --build -d
   ```

4. **Acesse a documentação interativa (Swagger):**
   👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🔒 Segurança e Boas Práticas

* **Credenciais:** O repositório está configurado (via `.gitignore`) para nunca versionar o arquivo `.env`. Nunca faça commit de senhas, tokens ou chaves de API.
* **Testes na API:** O projeto inclui um arquivo `api-testes.http` com templates de requisições que podem ser usados no VS Code (REST Client) ou IntelliJ. Lembre-se de substituir o placeholder do JWT pelo seu token real gerado no login.

---

## 🤖 Pipeline de CI/CD

O repositório conta com um fluxo de **Integração Contínua** via GitHub Actions. A cada `push` para a branch `main`:
* Uma máquina virtual Linux é provisionada.
* Instâncias reais de **PostgreSQL** e **Redis** são levantadas em contêineres.
* O pipeline executa o ciclo completo de build e validação de todos os testes automatizados.

---

👨‍💻 Desenvolvido por **Erick Levy Barbosa dos Santos** 🚀
