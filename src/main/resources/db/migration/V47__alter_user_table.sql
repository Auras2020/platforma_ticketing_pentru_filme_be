ALTER TABLE user_account
    ADD COLUMN theatre_id INTEGER;
ALTER TABLE user_account
    ADD CONSTRAINT user_account_theatre_id_fkey FOREIGN KEY (theatre_id) REFERENCES theatre(id);
