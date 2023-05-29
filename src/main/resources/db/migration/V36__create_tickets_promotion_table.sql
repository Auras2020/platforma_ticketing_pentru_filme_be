DROP TABLE IF EXISTS tickets_promotion;
CREATE TABLE tickets_promotion
(
    id serial PRIMARY KEY,
    nrTickets integer NOT NULL,
    reduction integer NOT NULL,
    show_timing_id INTEGER NOT NULL
);

ALTER TABLE tickets_promotion
    ADD CONSTRAINT tickets_promotion_show_timing_id_fkey FOREIGN KEY (show_timing_id) REFERENCES show_timing(id);

ALTER TABLE people_promotion
    ADD CONSTRAINT people_promotion_show_timing_id_fkey FOREIGN KEY (show_timing_id) REFERENCES show_timing(id);
