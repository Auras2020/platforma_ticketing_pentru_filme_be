ALTER TABLE show_timing
    ALTER COLUMN start_date SET DATA TYPE timestamp without time zone;
ALTER TABLE show_timing
    ALTER COLUMN end_date SET DATA TYPE timestamp without time zone;
ALTER TABLE show_timing
    ALTER COLUMN day SET DATA TYPE timestamp without time zone;
