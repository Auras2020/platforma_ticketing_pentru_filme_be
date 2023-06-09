ALTER TABLE tickets_promotion
    DROP COLUMN IF EXISTS nrtickets;
ALTER TABLE products_promotion
    DROP COLUMN IF EXISTS nrproducts;
