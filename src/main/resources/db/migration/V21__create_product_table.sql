DROP TABLE IF EXISTS product;
CREATE TABLE product
(
    id serial PRIMARY KEY,
    name varchar(256) NOT NULL,
    category varchar(256) NOT NULL,
    price INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    number INTEGER NOT NULL
);
