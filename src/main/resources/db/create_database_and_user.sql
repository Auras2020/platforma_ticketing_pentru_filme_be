CREATE DATABASE platforma_ticketing;

create user ticketing with encrypted password 'ticketing';

grant all privileges on database platforma_ticketing to ticketing;
