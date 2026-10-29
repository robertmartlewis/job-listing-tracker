ALTER TABLE job_listing
    ADD COLUMN rf_id character varying(23) NOT NULL,
    ADD CONSTRAINT uk_job_listing_rf_id UNIQUE (rf_id);