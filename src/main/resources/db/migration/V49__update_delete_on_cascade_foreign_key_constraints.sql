ALTER TABLE show_schedule
    DROP CONSTRAINT show_timing_theatre_id_fkey;
ALTER TABLE show_schedule
    DROP CONSTRAINT show_timing_movie_id_fkey;
ALTER TABLE show_schedule
    ADD CONSTRAINT show_timing_theatre_id_fkey FOREIGN KEY (theatre_id) REFERENCES theatre(id) ON DELETE CASCADE;
ALTER TABLE show_schedule
    ADD CONSTRAINT show_timing_movie_id_fkey FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE;

ALTER TABLE venue
    DROP CONSTRAINT venue_theatre_id_fkey;
ALTER TABLE venue
    ADD CONSTRAINT venue_theatre_id_fkey FOREIGN KEY (theatre_id) REFERENCES theatre(id) ON DELETE CASCADE;

ALTER TABLE show_schedule
    DROP CONSTRAINT show_timing_venue_id_fkey;
ALTER TABLE show_schedule
    ADD CONSTRAINT show_timing_venue_id_fkey FOREIGN KEY (venue_id) REFERENCES venue(id) ON DELETE CASCADE;

ALTER TABLE product
    DROP CONSTRAINT product_theatre_id_fkey;
ALTER TABLE product
    ADD CONSTRAINT product_theatre_id_fkey FOREIGN KEY (theatre_id) REFERENCES theatre(id) ON DELETE CASCADE;

ALTER TABLE orders
    DROP CONSTRAINT orders_show_timing_id_fkey;
ALTER TABLE orders
    DROP CONSTRAINT orders_user_id_fkey;
ALTER TABLE orders
    DROP CONSTRAINT orders_product_id_fkey;
ALTER TABLE orders
    ADD CONSTRAINT orders_show_timing_id_fkey FOREIGN KEY (show_timing_id) REFERENCES show_schedule(id) ON DELETE CASCADE;
ALTER TABLE orders
    ADD CONSTRAINT orders_user_id_fkey FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE;
ALTER TABLE orders
    ADD CONSTRAINT orders_product_id_fkey FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE;

ALTER TABLE tickets_promotion
    DROP CONSTRAINT tickets_promotion_show_timing_id_fkey;
ALTER TABLE tickets_promotion
    ADD CONSTRAINT tickets_promotion_show_timing_id_fkey FOREIGN KEY (show_timing_id) REFERENCES show_schedule(id) ON DELETE CASCADE;

ALTER TABLE people_promotion
    DROP CONSTRAINT people_promotion_show_timing_id_fkey;
ALTER TABLE people_promotion
    ADD CONSTRAINT people_promotion_show_timing_id_fkey FOREIGN KEY (show_timing_id) REFERENCES show_schedule(id) ON DELETE CASCADE;

ALTER TABLE products_promotion
    DROP CONSTRAINT products_promotion_show_timing_id_fkey;
ALTER TABLE products_promotion
    ADD CONSTRAINT products_promotion_show_timing_id_fkey FOREIGN KEY (show_timing_id) REFERENCES show_schedule(id) ON DELETE CASCADE;

ALTER TABLE movie_genres
    DROP CONSTRAINT movie_genres_movie_id_fkey;
ALTER TABLE movie_genres
    DROP CONSTRAINT movie_genres_genre_id_fkey;
ALTER TABLE movie_genres
    ADD CONSTRAINT movie_genres_movie_id_fkey FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE;
ALTER TABLE movie_genres
    ADD CONSTRAINT movie_genres_genre_id_fkey FOREIGN KEY (genre_id) REFERENCES genre(id) ON DELETE CASCADE;

ALTER TABLE user_account
    DROP CONSTRAINT user_account_theatre_id_fkey;
ALTER TABLE user_account
    ADD CONSTRAINT user_account_theatre_id_fkey FOREIGN KEY (theatre_id) REFERENCES theatre(id) ON DELETE CASCADE;
