# Projeto Prático – Processo Seletivo SEPLAG 2026

Este repositório contém a implementação do **Projeto Prático – Back End Java Sênior**, conforme exigido no **Processo
Seletivo Conjunto nº 001/2026/SEPLAG e demais Órgãos**.

## Dados do Candidato

- **Nome:** CARLOS EDUARDO RIBEIRO BRUNORO
- **CPF:** 942.885.262-72
- **Nº de Inscrição:** 16366

## Vaga

- **Cargo:** Engenheiro da Computação – Sênior
- **Processo Seletivo:** Conjunto nº 001/2026/SEPLAG e demais Órgãos

## Escopo da Prova

Este projeto refere-se ao **ANEXO II-A – Projeto Desenvolvedor Back End**, correspondente ao:

**PROJETO PRÁTICO – IMPLEMENTAÇÃO BACK END JAVA SÊNIOR**

O escopo da avaliação é **exclusivamente Back End**, não contemplando desenvolvimento de Front End, conforme definido no
edital do processo seletivo.

## Sobre o Projeto

O objetivo deste projeto é demonstrar conhecimentos técnicos e boas práticas no desenvolvimento de APIs REST utilizando
Java e Spring Boot, incluindo organização de código, segurança, persistência de dados, versionamento de banco e execução
em ambiente containerizado.

As instruções detalhadas para **execução**, **configuração** e **testes** da aplicação estão descritas nas seções abaixo
deste documento.

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

## Endpoints (documentação)

A API possui documentação OpenAPI/Swagger. Quando a aplicação estiver rodando localmente, acesse a URL do Swagger (
geralmente algo como `http://localhost:8080/swagger-ui.html` ou `/swagger-ui/index.html`) para ver rotas, modelos e
testar endpoints.

> Observação: o caminho exato depende das configurações do projeto e do contexto (server.port, context-path, etc.).

## MinIO Local (Servidor de Objetos)

Este projeto utiliza o **MinIO** como storage de arquivos, compatível com S3.

---

## Acessando a interface web

- URL: [http://localhost:9001/login](http://localhost:9001/login)
- Login / Senha:
    - **Access Key:** `minioadmin`
    - **Secret Key:** `minioadmin`

> A interface web permite criar buckets, enviar arquivos e visualizar o conteúdo.

## Bucket padrão

Para facilitar a avaliação e agilizar o processo, foi criado um bucket chamado **`imagens`** de forma **automática na
inicialização do projeto**.  
Dessa forma, não é necessário criar o bucket manualmente no MinIO.

No arquivo `application.yml`, configure o MinIO:

```yaml
integrations:
  minio:
    endpoint: http://localhost:9000
    access-key: minioadmin
    secret-key: minioadmin
    bucket-name: imagens
```
---

## Recuperação por link pré-assinado

A aplicação disponibiliza a recuperação de arquivos por meio de **links pré-assinados**, compatíveis com S3/MinIO.

Esses links são gerados pelo backend e possuem **tempo de expiração configurável**, permitindo o download direto do arquivo sem necessidade de autenticação adicional.  
O valor padrão é **30 minutos**, ajustável pela propriedade `signature-duration` no `application.yml`:

```yaml
integrations:
  minio:
    signature-duration: 30m
```
---

## Teste de WebSocket – Notificação de Novo Álbum

Esta página tem como objetivo **validar a comunicação via WebSocket (STOMP + SockJS)**, responsável por notificar o front-end sempre que um novo álbum for cadastrado.

### Acesso
Após iniciar a aplicação, acesse:
- http://localhost:8080/index.html

### Funcionamento
1. Ao abrir a página, a conexão WebSocket é estabelecida automaticamente (**status: Conectado**).
2. O cliente fica inscrito no tópico `/topic/novo-album`.
3. Quando um novo álbum é cadastrado com sucesso no back-end, uma notificação é enviada e exibida em tempo real na tela.

### Observação
A página é utilizada apenas para **teste técnico da funcionalidade WebSocket**, não representando a interface final do sistema.


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