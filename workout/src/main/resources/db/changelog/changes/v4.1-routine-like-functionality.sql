--liquibase formatted sql

--changeset TymofiiSkrypko:create-routine_likes-table
CREATE TABLE routine_like
(
    routine_id     UUID NOT NULL,
    user_id        UUID NOT NULL,  -- Reference to user_id in another database schema; no FK constraint
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_routine_like PRIMARY KEY (routine_id, user_id),
    CONSTRAINT fk_routine_like_on_routine FOREIGN KEY (routine_id) REFERENCES routine (id) ON DELETE CASCADE
);

--changeset TymofiiSkrypko:add-routine-likes-count-column
ALTER TABLE routine
    ADD COLUMN likes_count BIGINT DEFAULT 0;

--changeset TymofiiSkrypko:create-increment-likes-trigger-function
CREATE OR REPLACE FUNCTION increment_likes_count()
    RETURNS TRIGGER AS '
    BEGIN
        UPDATE routine
        SET likes_count = likes_count + 1
        WHERE id = NEW.routine_id;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

--changeset TymofiiSkrypko:create-decrement-likes-trigger-function
CREATE OR REPLACE FUNCTION decrement_likes_count()
    RETURNS TRIGGER AS '
    BEGIN
        UPDATE routine
        SET likes_count = GREATEST(0, likes_count - 1)  -- Ensures count does not drop below zero
        WHERE id = OLD.routine_id;
        RETURN OLD;
    END;
' LANGUAGE plpgsql;

--changeset TymofiiSkrypko:create-after-like-insert-trigger
CREATE TRIGGER after_like_insert
    AFTER INSERT ON routine_like
    FOR EACH ROW
EXECUTE FUNCTION increment_likes_count();

--changeset TymofiiSkrypko:create-after-like-delete-trigger
CREATE TRIGGER after_like_delete
    AFTER DELETE ON routine_like
    FOR EACH ROW
EXECUTE FUNCTION decrement_likes_count();
