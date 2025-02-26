--liquibase formatted sql

--changeset ValentynBondarenko:create_table_like_notification_of_routine
CREATE TABLE IF NOT EXISTS user_profiles.like_notification
(
    id                   UUID                        NOT NULL,
    created_at           TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    modified_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by           UUID                        NOT NULL,
    modified_by          UUID                        NOT NULL,
    owner_id             UUID                        NOT NULL,
    routine_id           UUID                        NOT NULL,
    likes_count          INTEGER                     NOT NULL,
    CONSTRAINT pk_like_notification PRIMARY KEY (id)
);
