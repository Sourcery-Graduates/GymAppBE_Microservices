--liquibase formatted sql

--changeset przemyslawren:update-routine-description-type
ALTER TABLE workout_data.routine
ALTER COLUMN description TYPE VARCHAR(3000);