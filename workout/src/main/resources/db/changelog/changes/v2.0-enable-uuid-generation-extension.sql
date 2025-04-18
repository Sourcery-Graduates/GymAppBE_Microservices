--liquibase formatted sql

--changeset ValentynBondarenko:enable_extension_uuid_generation
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
