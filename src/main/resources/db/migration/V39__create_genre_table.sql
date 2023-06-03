DROP TABLE IF EXISTS genre;
CREATE TABLE genre
(
    id serial PRIMARY KEY,
    name character varying(50) NOT NULL
);

INSERT INTO genre (id, name)
VALUES (1, 'Action');
INSERT INTO genre (id, name)
VALUES (2, 'Adventure');
INSERT INTO genre (id, name)
VALUES (3, 'Comedy');
INSERT INTO genre (id, name)
VALUES (4, 'Drama');
INSERT INTO genre (id, name)
VALUES (5, 'Horror');
INSERT INTO genre (id, name)
VALUES (6, 'Romance');
INSERT INTO genre (id, name)
VALUES (7, 'SF');
INSERT INTO genre (id, name)
VALUES (8, 'Thriller');
INSERT INTO genre (id, name)
VALUES (9, 'Western');
INSERT INTO genre (id, name)
VALUES (10, 'Crime');
INSERT INTO genre (id, name)
VALUES (11, 'Mystery');
INSERT INTO genre (id, name)
VALUES (12, 'Family');
INSERT INTO genre (id, name)
VALUES (13, 'Animation');
INSERT INTO genre (id, name)
VALUES (14, 'Fantasy');

CREATE SEQUENCE IF NOT EXISTS genre_id_seq
    START WITH 1
    INCREMENT BY 1;
alter sequence genre_id_seq restart with 15;
