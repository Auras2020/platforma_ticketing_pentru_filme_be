DROP TABLE IF EXISTS review;
CREATE TABLE review
(
    id serial PRIMARY KEY,
    name varchar(2000) NOT NULL,
    created_date timestamp without time zone NOT NULL,
    user_id INTEGER NOT NULL,
    movie_id INTEGER NOT NULL
);
