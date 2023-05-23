ALTER TABLE booked_product
    DROP COLUMN name;

ALTER TABLE booked_product
    ADD COLUMN product_id INTEGER;
ALTER TABLE booked_product
    ADD CONSTRAINT booked_product_product_id_fkey FOREIGN KEY (product_id) REFERENCES product(id);
