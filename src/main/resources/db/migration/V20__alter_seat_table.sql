ALTER TABLE seat
    ADD COLUMN user_id INTEGER;
ALTER TABLE seat
    ADD CONSTRAINT seat_user_id_fkey FOREIGN KEY (user_id) REFERENCES user_account(id);
