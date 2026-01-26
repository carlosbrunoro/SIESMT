CREATE TABLE artista_album
(
    id         BIGSERIAL PRIMARY KEY,
    artista_id BIGSERIAL NOT NULL,
    album_id   BIGSERIAL NOT NULL
);

ALTER TABLE artista_album
    ADD CONSTRAINT fk_artista_album_artista
        FOREIGN KEY (artista_id) REFERENCES artista (id);

ALTER TABLE artista_album
    ADD CONSTRAINT fk_artista_album_album
        FOREIGN KEY (album_id) REFERENCES album (id);

ALTER TABLE artista_album
    ADD CONSTRAINT uq_artista_album UNIQUE (artista_id, album_id);