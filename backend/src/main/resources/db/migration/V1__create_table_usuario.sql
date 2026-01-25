CREATE TABLE usuario
(
    id        SERIAL PRIMARY KEY,
    username  VARCHAR(100) NOT NULL UNIQUE,
    senha     VARCHAR(255) NOT NULL,
    status    VARCHAR(30)  NOT NULL,
    criado_em TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE usuario
    ADD CONSTRAINT chk_status_usuario
        CHECK (status IN ('ATIVO', 'BLOQUEADO', 'INATIVO'));