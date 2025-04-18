--liquibase formatted sql

--changeset przemyslawren:update-routine_exercise-default-values-not-null
ALTER TABLE routine_exercise
ALTER COLUMN default_sets SET NOT NULL,
ALTER COLUMN default_reps SET NOT NULL,
ALTER COLUMN default_weight SET NOT NULL,
ALTER COLUMN default_rest_time SET NOT NULL;
