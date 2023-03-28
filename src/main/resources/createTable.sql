DROP TABLE IF EXISTS composition;
DROP TABLE IF EXISTS album;
DROP TABLE IF EXISTS artist;

DROP SEQUENCE IF EXISTS composition_id_seq;
DROP SEQUENCE IF EXISTS album_id_seq;
DROP SEQUENCE IF EXISTS artist_id_seq;

CREATE SEQUENCE artist_id_seq START 1 INCREMENT 1;
CREATE SEQUENCE album_id_seq START 1 INCREMENT 1;
CREATE SEQUENCE composition_id_seq START 1 INCREMENT 1;

CREATE TABLE artist (
    id INTEGER UNIQUE DEFAULT nextval('artist_id_seq'),
    name VARCHAR(254) NOT NULL
);

CREATE TABLE album (
    id INTEGER UNIQUE DEFAULT nextval('album_id_seq'),
    name VARCHAR(254) NOT NULL,
    genre VARCHAR(254) NOT NULL,
    artist_id INTEGER,
    FOREIGN KEY (artist_id) REFERENCES artist(id)
);

CREATE TABLE composition (
    id INTEGER UNIQUE DEFAULT nextval('composition_id_seq'),
    name VARCHAR(254) NOT NULL,
    duration INTEGER NOT NULL,
    album_id INTEGER,
    prev_listening_id INTEGER,
    FOREIGN KEY (album_id) REFERENCES album(id)
);
