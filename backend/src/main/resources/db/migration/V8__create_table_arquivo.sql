CREATE TABLE arquivo
(
    id            BIGSERIAL PRIMARY KEY,
    storage_key   TEXT      NOT NULL,
    nome_original VARCHAR(255),
    tipo_mime     VARCHAR(100),
    tamanho       BIGINT,
    criado_em     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_arquivo_storage_key ON arquivo (storage_key);
CREATE INDEX idx_arquivo_nome ON arquivo (nome_original);