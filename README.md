# 💰 Finance CLI

Sistema de gerenciamento financeiro desenvolvido em **Java e Spring Boot**, executado diretamente pelo terminal.

O projeto foi desenvolvido com o objetivo de praticar conceitos de programação orientada a objetos, persistência de dados, integração com banco de dados e arquitetura em camadas utilizando o ecossistema Java.

## 🚀 Funcionalidades

- [x] Cadastrar lançamentos financeiros.
- [x] Listar todos os lançamentos.
- [x] Buscar lançamento pelo ID.
- [x] Atualizar lançamentos existentes.
- [x] Excluir lançamentos pelo ID.
- [x] Validação básica dos dados informados.

## 🛠️ Tecnologias utilizadas

- **Java 25** — Linguagem de programação.
- **Spring Boot 4.1.1** — Framework para desenvolvimento da aplicação.
- **Spring Data JPA** — Integração e gerenciamento da persistência de dados.
- **Hibernate** — Mapeamento objeto-relacional (ORM).
- **PostgreSQL 17** — Banco de dados relacional.
- **Docker** — Execução do banco de dados em container.
- **Maven** — Gerenciamento de dependências e execução do projeto.

## 📂 Estrutura do projeto

```text
src/
└── main/
    ├── java/
    │   └── com/
    │       └── finance/
    │           ├── entity/
    │           │   └── Lancamento.java
    │           ├── repository/
    │           │   └── LancamentoRepository.java
    │           ├── service/
    │           │   └── LancamentoService.java
    │           └── FinanceApplication.java
    │
    └── resources/
        └── application.properties
```

## 🏗️ Arquitetura

O projeto utiliza uma organização em camadas para separar as responsabilidades:

- **Entity:** representa os dados e o mapeamento das entidades no banco de dados.
- **Repository:** responsável pelo acesso e pelas operações no banco de dados.
- **Service:** concentra a lógica de negócio da aplicação.
- **FinanceApplication:** inicia a aplicação e gerencia a interação com o usuário pelo terminal.

## ⚙️ Como executar o projeto

### Pré-requisitos

- Java JDK 25 ou compatível com a configuração do projeto.
- Docker Desktop.
- Git.

### 1. Clone o repositório

```bash
git clone https://github.com/Thiago-S-Fukushima/Finance-CLI.git
```

### 2. Acesse a pasta do projeto

```bash
cd Finance-CLI/demo
```

### 3. Inicie o banco de dados

Com o Docker Desktop aberto, execute:

```bash
docker compose up -d
```

### 4. Execute a aplicação

No Windows, utilize:

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux ou macOS:

```bash
./mvnw spring-boot:run
```

## 💻 Exemplo de utilização

Ao iniciar a aplicação, o usuário encontrará o seguinte menu:

```text
===== FINANCE CLI =====

1 - Cadastrar lançamento
2 - Listar lançamento
3 - Excluir lançamento
4 - Buscar lançamento por ID
5 - Atualizar lançamento
0 - Sair

Escolha uma opção:
```

## 📚 Conhecimentos praticados

Durante o desenvolvimento, foram trabalhados conceitos como:

- Programação Orientada a Objetos (POO).
- Injeção de Dependência (DI).
- Organização de código em camadas.
- Operações CRUD.
- Persistência de dados com JPA.
- Utilização de `BigDecimal` para valores monetários.
- Utilização de `LocalDate` para datas.
- Tratamento de exceções com `try/catch`.
- Utilização de `Optional` para consultas.
- Integração entre Java e PostgreSQL.

## 🎯 Objetivo

Este projeto faz parte da minha jornada de aprendizado em desenvolvimento Back-End, com foco em Java e Spring Boot.

A proposta é aplicar conhecimentos na prática, desenvolver soluções funcionais e evoluir continuamente minhas habilidades como desenvolvedor.

---

**Desenvolvido por Thiago Fukushima**

[GitHub](https://github.com/Thiago-S-Fukushima)
