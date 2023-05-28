DROP TABLE IF EXISTS people_promotion;
CREATE TABLE people_promotion
(
    id serial PRIMARY KEY,
    adult integer NOT NULL,
    student integer NOT NULL,
    child integer NOT NULL
);
