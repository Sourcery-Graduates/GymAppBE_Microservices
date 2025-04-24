--liquibase formatted sql

--changeset przemyslawren:update-routine-description-type
ALTER TABLE routine
ALTER COLUMN description TYPE VARCHAR(3000);
