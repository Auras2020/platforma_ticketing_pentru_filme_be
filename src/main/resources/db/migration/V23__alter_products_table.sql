ALTER TABLE product
    ADD COLUMN theatre_id INTEGER;
ALTER TABLE product
    ADD CONSTRAINT product_theatre_id_fkey FOREIGN KEY (theatre_id) REFERENCES theatre(id);
