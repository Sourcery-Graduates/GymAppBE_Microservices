--liquibase formatted sql

--changeset przemyslawren:create-mock-schemas
CREATE SCHEMA IF NOT EXISTS user_auth;
CREATE SCHEMA IF NOT EXISTS user_profiles;
CREATE SCHEMA IF NOT EXISTS workout_data;
CREATE SCHEMA IF NOT EXISTS exercises;