DROP TABLE IF EXISTS show_timing;
CREATE TABLE show_timing
(
    id serial PRIMARY KEY,
    theatre_id INTEGER NOT NULL,
    movie_id INTEGER NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    time varchar(256) NOT NULL,
    price integer NOT NULL
);

ALTER TABLE show_timing
    ADD CONSTRAINT show_timing_theatre_id_fkey FOREIGN KEY (theatre_id) REFERENCES theatre(id);
ALTER TABLE show_timing
    ADD CONSTRAINT show_timing_movie_id_fkey FOREIGN KEY (movie_id) REFERENCES movie(id);
