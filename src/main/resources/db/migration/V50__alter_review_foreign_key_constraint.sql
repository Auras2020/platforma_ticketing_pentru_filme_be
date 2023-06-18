ALTER TABLE review
    DROP CONSTRAINT fk8378dhlpvq0e9tnkn9mx0r64i;
ALTER TABLE review
    ADD CONSTRAINT review_movie_id_fkey FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE;
