CREATE TABLE imagem_album
(
    id        SERIAL PRIMARY KEY,
    album_id  INTEGER      NOT NULL,
    url       VARCHAR(500) NOT NULL,
    criado_em TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_imagem_album_album
        FOREIGN KEY (album_id)
            REFERENCES album (id)
);