CREATE TABLE artista
(
    id        SERIAL PRIMARY KEY,
    nome      VARCHAR(200) NOT NULL,
    tipo      VARCHAR(20)  NOT NULL,
    criado_em TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE artista
    ADD CONSTRAINT chk_artista_tipo
        CHECK (tipo IN ('CANTOR', 'BANDA'));