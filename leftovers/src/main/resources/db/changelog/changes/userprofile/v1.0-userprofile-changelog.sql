--liquibase formatted sql

--changeset PiotrBaranowski:table_userprofiles.profiles_Create_if_not_exists
CREATE TABLE IF NOT EXISTS user_profiles.profiles(
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    modified_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by UUID NOT NULL,
    modified_by UUID NOT NULL,
    user_id UUID NOT NULL UNIQUE,
    username VARCHAR(32) NOT NULL UNIQUE,
    first_name VARCHAR(64),
    last_name VARCHAR(64),
    bio TEXT,
    avatar_url TEXT,
    location VARCHAR(128),
    com.sourcery.gymapp.backend.workout.settings JSONB
);

--changeset PiotrBaranowski:table_userprofiles.profiles_Add_Not_Null_for_firstName
ALTER TABLE user_profiles.profiles
ALTER COLUMN first_name SET NOT NULL;

--changeset PiotrBaranowski:table_userprofiles.profiles_Add_Not_Null_for_lastName
ALTER TABLE user_profiles.profiles
ALTER COLUMN last_name SET NOT NULL;