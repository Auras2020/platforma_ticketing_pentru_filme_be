DROP TABLE IF EXISTS movie_genres;
CREATE TABLE movie_genres
(
    id       serial  NOT NULL,
    movie_id integer NOT NULL,
    genre_id integer NOT NULL
);

ALTER TABLE movie_genres
    ADD CONSTRAINT movie_genres_movie_id_fkey FOREIGN KEY (movie_id) REFERENCES movie(id);

ALTER TABLE movie_genres
    ADD CONSTRAINT movie_genres_genre_id_fkey FOREIGN KEY (genre_id) REFERENCES genre(id);
