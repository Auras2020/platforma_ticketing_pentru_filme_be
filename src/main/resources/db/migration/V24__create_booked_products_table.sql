DROP TABLE IF EXISTS booked_product;
CREATE TABLE booked_product
(
    id serial PRIMARY KEY,
    show_timing_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    name varchar(256) NOT NULL,
    quantity INTEGER NOT NULL,
    number INTEGER NOT NULL
);

ALTER TABLE booked_product
    ADD CONSTRAINT booked_product_show_timing_id_fkey FOREIGN KEY (show_timing_id) REFERENCES show_timing(id);
ALTER TABLE booked_product
    ADD CONSTRAINT booked_product_user_id_fkey FOREIGN KEY (user_id) REFERENCES user_account(id);
