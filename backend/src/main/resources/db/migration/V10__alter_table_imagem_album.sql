ALTER TABLE imagem_album
    DROP COLUMN album_id;

ALTER TABLE imagem_album
    DROP COLUMN url;

ALTER TABLE imagem_album
    DROP COLUMN criado_em;

ALTER TABLE imagem_album
    ADD COLUMN album_id BIGINT NOT NULL;

ALTER TABLE imagem_album
    ADD COLUMN arquivo_id BIGINT NOT NULL;

ALTER TABLE imagem_album
    ADD COLUMN criado_em TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now();

ALTER TABLE imagem_album
    ADD CONSTRAINT fk_imagem_album_album
        FOREIGN KEY (album_id) REFERENCES album (id);

ALTER TABLE imagem_album
    ADD CONSTRAINT fk_imagem_album_arquivo
        FOREIGN KEY (arquivo_id) REFERENCES arquivo (id);

CREATE INDEX idx_imagem_album_album_id
    ON imagem_album (album_id);

CREATE INDEX idx_imagem_album_arquivo_id
    ON imagem_album (arquivo_id);