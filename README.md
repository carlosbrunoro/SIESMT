# Catálogo Musical API

API REST desenvolvida em **Java com Spring Boot**, como parte do **Projeto Prático – Implementação Back End Java Sênior**, com foco em organização, clareza arquitetural, segurança, boas práticas e aderência aos requisitos do edital.

---

## Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Security
- JWT (Access Token + Refresh Token)
- PostgreSQL
- Flyway Migrations
- Docker / Docker Compose
- OpenAPI / Swagger
- JPA / Hibernate

---

## Banco de Dados

O projeto utiliza PostgreSQL. Para subir o banco localmente via Docker:

```bash
docker compose up -d
```

## Autenticação

A autenticação foi implementada utilizando **JWT (JSON Web Token)**, com tempo de expiração de **5 minutos**, conforme exigido no edital.
Para simplificar o escopo do projeto prático, foi utilizado um **usuário fixo**, apenas para fins de demonstração do fluxo de autenticação e renovação do token, sem foco no gerenciamento completo de usuários.

### Credenciais de Acesso (Demonstração)

- **Usuário:** `admin`
- **Senha:** `admin`

> ⚠️ As credenciais acima são utilizadas exclusivamente para fins de demonstração no contexto do projeto prático.

O Flyway foi utilizado para versionamento do schema do banco de dados, adotando scripts SQL versionados conforme boas práticas. Cada versão representa uma evolução incremental do modelo de dados, garantindo rastreabilidade e facilidade de manutenção.

## Rate Limit

A arquitetura foi preparada para inclusão de rate limit por IP ou usuário autenticado,
utilizando filtros HTTP antes da cadeia de autenticação do Spring Security.