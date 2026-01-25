-- USUÁRIO EXEMPLO
INSERT INTO usuario (username, senha, status)
VALUES ('admin', '$2a$12$yWJFOHgbVd7iCmTC3LtUxupG1U//uv6cie.E.ou9eu1lBcV23laHy', 'ATIVO');

-- ARTISTAS
INSERT INTO artista (nome, tipo)
VALUES ('Serj Tankian', 'CANTOR'),
       ('Mike Shinoda', 'CANTOR'),
       ('Michel Teló', 'CANTOR'),
       ('Guns N’ Roses', 'BANDA');

-- ÁLBUNS
INSERT INTO album (nome)
VALUES ('Harakiri'),
       ('Black Blooms'),
       ('Post Traumatic'),
       ('Use Your Illusion I'),
       ('Use Your Illusion II');

-- RELACIONAMENTO ARTISTA ↔ ÁLBUM
INSERT INTO artista_album (artista_id, album_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (4, 4),
       (4, 5);
