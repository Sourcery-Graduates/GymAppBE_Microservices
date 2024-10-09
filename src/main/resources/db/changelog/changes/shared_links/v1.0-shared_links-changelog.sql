--liquibase formatted sql

--changeset przemyslawren:create-shared-links
CREATE TABLE IF NOT EXISTS shared_links.shared_links (
    id              UUID PRIMARY KEY,
    user_id         UUID,
    routine_id      UUID,
    link            VARCHAR(256) NOT NULL,
    expires_at      TIMESTAMP NOT NULL,
    is_active       BOOLEAN,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    modified_at     TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by      UUID NOT NULL,
    modified_by     UUID NOT NULL
);

CREATE TABLE IF NOT EXISTS shared_links.link_access_logs (
    access_id           UUID PRIMARY KEY,
    link_id             UUID NOT NULL,
    accessed_at         TIMESTAMP NOT NULL DEFAULT NOW(),
    accessed_by_user_id UUID,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    modified_at         TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by          UUID NOT NULL,
    modified_by         UUID NOT NULL
)