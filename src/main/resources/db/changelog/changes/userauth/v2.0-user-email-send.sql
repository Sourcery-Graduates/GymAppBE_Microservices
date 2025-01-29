--liquibase formatted sql

--changeset PiotrBaranowski:add-isEnabled-column-to-user-table
ALTER TABLE user_auth.users ADD COLUMN is_enabled BOOLEAN DEFAULT false NOT NULL;

--changeset PiotrBaranowski:add-unique-constraint-to-email-column-user-table
ALTER TABLE user_auth.users ADD CONSTRAINT unique_email UNIQUE (email);

--changeset PiotrBaranowski:add-emailToken_table
CREATE TABLE user_auth.email_token
(
    id              UUID PRIMARY KEY,
    type            VARCHAR(36),
    user_id         UUID,
    token           VARCHAR(36) UNIQUE,
    expiration_time TIMESTAMP WITH TIME ZONE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    modified_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);