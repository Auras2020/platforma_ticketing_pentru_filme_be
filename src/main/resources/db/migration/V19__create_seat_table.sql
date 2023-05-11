DROP TABLE IF EXISTS seat;
CREATE TABLE seat
(
    id serial PRIMARY KEY,
    show_timing_id INTEGER NOT NULL,
    seat varchar(256) NOT NULL
);

ALTER TABLE seat
    ADD CONSTRAINT seat_show_timing_id_fkey FOREIGN KEY (show_timing_id) REFERENCES show_timing(id);
