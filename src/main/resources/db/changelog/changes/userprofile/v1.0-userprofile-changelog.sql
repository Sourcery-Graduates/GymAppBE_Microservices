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
    settings JSONB
);