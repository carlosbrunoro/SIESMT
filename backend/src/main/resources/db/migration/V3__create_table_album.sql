CREATE TABLE album
(
    id             BIGSERIAL PRIMARY KEY,
    nome           VARCHAR(200) NOT NULL,
    ano_lancamento INTEGER,
    criado_em      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);