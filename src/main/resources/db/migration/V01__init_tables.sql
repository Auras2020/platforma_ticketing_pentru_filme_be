DROP TABLE IF EXISTS user_account;
CREATE TABLE user_account
(
    id serial PRIMARY KEY,
    name varchar(256) NOT NULL,
    email varchar(256) unique,
    password varchar(256)
);

DROP TABLE IF EXISTS role;
CREATE TABLE role
(
    role_id serial PRIMARY KEY,
    role varchar(256) NOT NULL
);
