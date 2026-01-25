CREATE TABLE album
(
    id             SERIAL PRIMARY KEY,
    nome           VARCHAR(200) NOT NULL,
    ano_lancamento INTEGER,
    criado_em      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);