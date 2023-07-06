ALTER TABLE review
    DROP CONSTRAINT fk69d1gmuhx8wws2qh6auu1amab;
ALTER TABLE review
    ADD CONSTRAINT review_user_id_fkey FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE;
