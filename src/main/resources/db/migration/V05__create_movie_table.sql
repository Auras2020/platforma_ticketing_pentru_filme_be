DROP TABLE IF EXISTS movie;
CREATE TABLE movie
(
    id serial PRIMARY KEY,
    name varchar(256) NOT NULL,
    rating integer,
    movie_poster bytea,
    genre varchar(256) NOT NULL,
    duration integer NOT NULL,
    actors varchar(256) NOT NULL,
    director varchar(256) NOT NULL,
    synopsis varchar(256),
    note double precision
);
