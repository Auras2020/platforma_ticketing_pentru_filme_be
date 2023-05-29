DROP TABLE IF EXISTS products_promotion;
CREATE TABLE products_promotion
(
    id serial PRIMARY KEY,
    nrProducts integer NOT NULL,
    reduction integer NOT NULL,
    show_timing_id INTEGER NOT NULL
);

ALTER TABLE products_promotion
    ADD CONSTRAINT products_promotion_show_timing_id_fkey FOREIGN KEY (show_timing_id) REFERENCES show_timing(id);
