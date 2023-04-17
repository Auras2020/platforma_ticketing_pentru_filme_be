DROP TABLE IF EXISTS theatre;
CREATE TABLE theatre
(
    id serial PRIMARY KEY,
    name varchar(256) NOT NULL,
    movie_poster bytea,
    location varchar(256) NOT NULL,
    address varchar(256) NOT NULL
);
