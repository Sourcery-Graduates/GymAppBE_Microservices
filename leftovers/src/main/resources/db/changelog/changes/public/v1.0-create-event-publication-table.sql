--liquibase formatted sql

--changeset ValentynBondarenko:create-table-event-publication-if_not-exists
CREATE TABLE IF NOT EXISTS public.event_publication
(
    id UUID NOT NULL,  -- UUID for event publication ID
    completion_date TIMESTAMPTZ,  -- Timestamp with timezone for when the event was completed
    event_type VARCHAR(255) NOT NULL,  -- Type of the event being published
    listener_id VARCHAR(255) NOT NULL,  -- ID of the listener handling the event
    publication_date TIMESTAMPTZ NOT NULL,  -- Timestamp when the event was published
    serialized_event TEXT NOT NULL,  -- Serialized form of the event for replay
    CONSTRAINT event_publication_pkey PRIMARY KEY (id)  -- Primary key constraint on ID
    );

--changeset ValentynBondarenko:alter-table-event-publication-change-owner-to-gym-user
ALTER TABLE IF EXISTS public.event_publication
    OWNER TO gym_app_user;
