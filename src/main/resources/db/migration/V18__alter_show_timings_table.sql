ALTER TABLE show_timing
    ADD COLUMN venue_id INTEGER;
ALTER TABLE show_timing
    ADD CONSTRAINT show_timing_venue_id_fkey FOREIGN KEY (venue_id) REFERENCES venue(id);
