DROP TABLE IF EXISTS booked_product;
DROP TABLE IF EXISTS seat;

DROP TABLE IF EXISTS orders;
CREATE TABLE orders
(
    id serial PRIMARY KEY,
    show_timing_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    seat varchar(256) NOT NULL,
    ticket_status varchar(256) NOT NULL,
    product_id INTEGER,
    number_products INTEGER,
    products_status varchar(256)
);

ALTER TABLE orders
    ADD CONSTRAINT orders_show_timing_id_fkey FOREIGN KEY (show_timing_id) REFERENCES show_timing(id);
ALTER TABLE orders
    ADD CONSTRAINT orders_user_id_fkey FOREIGN KEY (user_id) REFERENCES user_account(id);
ALTER TABLE orders
    ADD CONSTRAINT orders_product_id_fkey FOREIGN KEY (product_id) REFERENCES product(id);
