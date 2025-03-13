--liquibase formatted sql

--changeset TymofiiSkrypko:add-oauth2-support-to-user-table
ALTER TABLE user_auth.users
    ADD COLUMN provider VARCHAR(30),
    ADD COLUMN provider_id VARCHAR(255);

--changeset TymofiiSkrypko:remove-username-unique-constraint
ALTER TABLE user_auth.users
    DROP CONSTRAINT IF EXISTS users_username_key;
