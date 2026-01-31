ALTER TABLE regional
    DROP CONSTRAINT IF EXISTS regional_nome_key;

ALTER TABLE regional
    ADD COLUMN regional_id BIGINT;

ALTER TABLE regional
    ADD COLUMN criado_em TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now();

CREATE UNIQUE INDEX uq_regional_ativo_por_regional
    ON regional (regional_id)
    WHERE ativo = true;

CREATE UNIQUE INDEX uq_regional_nome_ativo
    ON regional (nome)
    WHERE ativo = true;

CREATE INDEX idx_regional_regional_id
    ON regional (regional_id);