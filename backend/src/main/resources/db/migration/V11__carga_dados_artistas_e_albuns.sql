-- =====================
-- ARTISTAS
-- =====================
INSERT INTO artista (nome, tipo)
VALUES
    ('Bruno Mars', 'CANTOR'),
    ('Taylor Swift', 'CANTOR'),
    ('Billie Eilish', 'CANTOR'),
    ('The Weeknd', 'CANTOR'),
    ('Rihanna', 'CANTOR'),
    ('Beyoncé', 'CANTOR'),
    ('Drake', 'CANTOR'),
    ('Imagine Dragons', 'BANDA'),
    ('Foo Fighters', 'BANDA'),
    ('Red Hot Chili Peppers', 'BANDA'),
    ('Arctic Monkeys', 'BANDA'),
    ('Pink Floyd', 'BANDA'),
    ('Queen', 'BANDA'),
    ('U2', 'BANDA'),
    ('Pearl Jam', 'BANDA'),
    ('Nirvana', 'BANDA'),
    ('Radiohead', 'BANDA'),
    ('The Beatles', 'BANDA'),
    ('Oasis', 'BANDA'),
    ('The Rolling Stones', 'BANDA');

-- =====================
-- ÁLBUNS
-- =====================
INSERT INTO album (nome)
VALUES
    ('24K Magic'),
    ('Midnights'),
    ('Happier Than Ever'),
    ('After Hours'),
    ('Anti'),
    ('Lemonade'),
    ('Scorpion'),
    ('Night Visions'),
    ('Wasting Light'),
    ('Californication'),
    ('AM'),
    ('The Dark Side of the Moon'),
    ('A Night at the Opera'),
    ('The Joshua Tree'),
    ('Ten'),
    ('Nevermind'),
    ('OK Computer'),
    ('Abbey Road'),
    ('(What’s the Story) Morning Glory?'),
    ('Sticky Fingers'),
    ('Echoes'),
    ('In Rainbows'),
    ('Let It Bleed');

-- =====================
-- RELACIONAMENTO ARTISTA ↔ ÁLBUM
-- (sem depender de IDs)
-- =====================
INSERT INTO artista_album (artista_id, album_id)
VALUES
    (
        (SELECT id FROM artista WHERE nome = 'Bruno Mars'),
        (SELECT id FROM album WHERE nome = '24K Magic')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'Taylor Swift'),
        (SELECT id FROM album WHERE nome = 'Midnights')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'Billie Eilish'),
        (SELECT id FROM album WHERE nome = 'Happier Than Ever')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'The Weeknd'),
        (SELECT id FROM album WHERE nome = 'After Hours')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'Rihanna'),
        (SELECT id FROM album WHERE nome = 'Anti')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'Beyoncé'),
        (SELECT id FROM album WHERE nome = 'Lemonade')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'Drake'),
        (SELECT id FROM album WHERE nome = 'Scorpion')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'Imagine Dragons'),
        (SELECT id FROM album WHERE nome = 'Night Visions')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'Foo Fighters'),
        (SELECT id FROM album WHERE nome = 'Wasting Light')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'Red Hot Chili Peppers'),
        (SELECT id FROM album WHERE nome = 'Californication')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'Arctic Monkeys'),
        (SELECT id FROM album WHERE nome = 'AM')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'Pink Floyd'),
        (SELECT id FROM album WHERE nome = 'The Dark Side of the Moon')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'Pink Floyd'),
        (SELECT id FROM album WHERE nome = 'Echoes')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'Queen'),
        (SELECT id FROM album WHERE nome = 'A Night at the Opera')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'U2'),
        (SELECT id FROM album WHERE nome = 'The Joshua Tree')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'Pearl Jam'),
        (SELECT id FROM album WHERE nome = 'Ten')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'Nirvana'),
        (SELECT id FROM album WHERE nome = 'Nevermind')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'Radiohead'),
        (SELECT id FROM album WHERE nome = 'OK Computer')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'Radiohead'),
        (SELECT id FROM album WHERE nome = 'In Rainbows')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'The Beatles'),
        (SELECT id FROM album WHERE nome = 'Abbey Road')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'Oasis'),
        (SELECT id FROM album WHERE nome = '(What’s the Story) Morning Glory?')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'The Rolling Stones'),
        (SELECT id FROM album WHERE nome = 'Sticky Fingers')
    ),
    (
        (SELECT id FROM artista WHERE nome = 'The Rolling Stones'),
        (SELECT id FROM album WHERE nome = 'Let It Bleed')
    );
