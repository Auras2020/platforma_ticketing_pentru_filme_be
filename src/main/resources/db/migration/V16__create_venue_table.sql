DROP TABLE IF EXISTS venue;
CREATE TABLE venue
(
    id serial PRIMARY KEY,
    location varchar(256) NOT NULL,
    theatre_id INTEGER NOT NULL,
    movie_id INTEGER NOT NULL,
    day date NOT NULL,
    time varchar(256) NOT NULL,
    venue_number integer NOT NULL,
    rows_number integer NOT NULL,
    columns_number integer NOT NULL
);

ALTER TABLE venue
    ADD CONSTRAINT venue_theatre_id_fkey FOREIGN KEY (theatre_id) REFERENCES theatre(id);
ALTER TABLE venue
    ADD CONSTRAINT venue_movie_id_fkey FOREIGN KEY (movie_id) REFERENCES movie(id);
