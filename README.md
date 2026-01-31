# Catálogo Musical API

API REST desenvolvida em **Java 21 + Spring Boot** como parte do Projeto Prático – Implementação Back End Java Sênior. O
objetivo deste README é explicar de forma mais clara como executar, configurar e entender os principais pontos da
aplicação.

## Visão geral

Aplicação responsável por gerenciar um catálogo musical (artistas, álbuns, imagens, regionais). Ela foi implementada com
foco em:

- Organização e separação de responsabilidades (controllers, services, repositories).
- Boas práticas de segurança (Spring Security + JWT).
- Versionamento do schema com Flyway.
- Execução local simplificada via Docker / Docker Compose.

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Security
- JWT (Access Token + Refresh Token)
- PostgreSQL
- Flyway Migrations
- Docker / Docker Compose
- OpenAPI / Swagger
- JPA / Hibernate

## Requisitos

Antes de rodar a aplicação localmente, tenha instalado:

- Docker & Docker Compose
- JDK 21
- Maven (para construir o backend, opcional se você usar o JAR já gerado)

## Execução rápida (ambiente de desenvolvimento)

1. Subir o banco PostgreSQL via Docker Compose:

```powershell
docker compose up -d
```

2. Rodar a aplicação backend (duas opções):

- Usando o JAR já gerado (diretório `backend/target`):

```powershell
java -jar backend\target\catalogo-musical-api-1.0.0.jar
```

- Compilar e executar com Maven (na raiz do projeto):

```powershell
mvn -f backend\pom.xml clean package
java -jar backend\target\catalogo-musical-api-1.0.0.jar
```

Observação: há um profile de staging com arquivo `application-staging.yml` em `backend/src/main/resources`.

## Configuração

As propriedades principais da aplicação estão em:

- `backend/src/main/resources/application.yml`
- `backend/src/main/resources/application-staging.yml`

Aqui você pode controlar (entre outros):

- Conexão com o banco de dados
- Propriedades de segurança e JWT
- Configurações do scheduler

Exemplo de configuração do scheduler presente no projeto:

```yaml
scheduler:
  regional-sync:
    enabled: true
    cron: "*/30 * * * * *"
```

## Autenticação (JWT)

A aplicação utiliza JWT para autenticação (Access + Refresh Tokens). Para simplificar o escopo do projeto prático,
existe um usuário fixo de demonstração.

Credenciais de demonstração:

- Usuário: `admin`
- Senha: `admin`

Observações importantes:

- O access token possui tempo de expiração curto (5 minutos) conforme os requisitos do projeto.
- O fluxo de renovação (refresh token) está implementado para demonstrar a troca de tokens sem reautenticação completa.

> ⚠️ Essas credenciais são apenas para demonstração local. Não use em produção.

## Endpoints (documentação)

A API possui documentação OpenAPI/Swagger. Quando a aplicação estiver rodando localmente, acesse a URL do Swagger (
geralmente algo como `http://localhost:8080/swagger-ui.html` ou `/swagger-ui/index.html`) para ver rotas, modelos e
testar endpoints.

> Observação: o caminho exato depende das configurações do projeto e do contexto (server.port, context-path, etc.).

## Rate Limit

A arquitetura foi preparada para incluir rate limiting por IP ou por usuário autenticado. A implementação está
organizada como filtros HTTP colocados antes da cadeia de autenticação do Spring Security.

## Scheduler — Sincronização de Regionais (Argus)

O projeto possui um scheduler configurável responsável por sincronizar as regionais locais com um endpoint externo (
Argus). A execução do scheduler é controlável via `application.yml` — é possível habilitar/desabilitar e ajustar o cron
sem alterar o código.

Localização relevante:

- Classe do agendamento: `src/main/java/br/gov/mt/seplag/schedule/RegionalSyncScheduler.java`
- Lógica de negócio da sincronização: `br/gov/mt/seplag/service/regional/RegionalSyncService.java`

## Migrations (Flyway)

As migrações SQL estão em `backend/src/main/resources/db/migration` e versionadas com o padrão do Flyway (ex.:
`V1__*.sql`, `V2__*.sql`, ...). Ao iniciar a aplicação, o Flyway aplica automaticamente as migrações no banco
configurado.

Arquivos de exemplo já presentes:

- V1__create_table_usuario.sql
- V2__create_table_artista.sql
- V3__create_table_album.sql
- ...

## Estrutura de diretórios (resumida)

- `backend/src/main/java` — código-fonte Java
- `backend/src/main/resources` — configurações, mensagens e migrations
- `backend/target` — artefatos gerados pelo Maven