--liquibase formatted sql

--changeset ValentynBondarenko:create_workout_data_exercise
CREATE TABLE exercise
(
    id          UUID                     NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    modified_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by  UUID                     NOT NULL,
    modified_by UUID                     NOT NULL,
    name        VARCHAR(255)             NOT NULL,
    force       VARCHAR(255),
    level       VARCHAR(255),
    mechanic    VARCHAR(255),
    equipment   VARCHAR(255),
    description TEXT[],
    category    VARCHAR(255),
    primary_muscles TEXT[],
    secondary_muscles TEXT[],
    images      TEXT[],
    CONSTRAINT pk_exercise PRIMARY KEY (id)
);

--changeset ValentynBondarenko:create_workout_data_routine
CREATE TABLE routine
(
    id          UUID                     NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    modified_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by  UUID                     NOT NULL,
    modified_by UUID                     NOT NULL,
    user_id     UUID                     NOT NULL,
    name        VARCHAR(255)             NOT NULL,
    description VARCHAR(255),
    CONSTRAINT pk_routine PRIMARY KEY (id)
);

--changeset ValentynBondarenko:create_workout_data_routine_exercise
CREATE TABLE routine_exercise
(
    id                UUID                     NOT NULL,
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    modified_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by        UUID                     NOT NULL,
    modified_by       UUID                     NOT NULL,
    routine_id        UUID                     NOT NULL,
    exercise_id       UUID                     NOT NULL,
    order_number      INTEGER                  NOT NULL,
    default_sets      INTEGER,
    default_reps      INTEGER,
    default_weight    DECIMAL(5, 2),
    default_rest_time INTEGER,
    notes             VARCHAR(255),
    CONSTRAINT pk_routine_exercise PRIMARY KEY (id)
);

--changeset ValentynBondarenko:create_workout_data_workout
CREATE TABLE workout
(
    id                  UUID                     NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    modified_at         TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by          UUID                     NOT NULL,
    modified_by         UUID                     NOT NULL,
    user_id             UUID                     NOT NULL,
    name                VARCHAR(255),
    date                TIMESTAMP WITH TIME ZONE,
    comment             VARCHAR(255),
    based_on_workout_id UUID,
    routine_id          UUID,
    CONSTRAINT pk_workout PRIMARY KEY (id)
);

--changeset ValentynBondarenko:create_workout_data_workout_exercise
CREATE TABLE workout_exercise
(
    id           UUID                     NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    modified_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by   UUID                     NOT NULL,
    modified_by  UUID                     NOT NULL,
    order_number INTEGER                  NOT NULL,
    notes        VARCHAR(255),
    exercise_id  UUID                     NOT NULL,
    workout_id   UUID                     NOT NULL,
    CONSTRAINT pk_workout_exercise PRIMARY KEY (id)
);

--changeset ValentynBondarenko:create_workout_data_workout_exercise_set
CREATE TABLE workout_exercise_set
(
    id                  UUID                     NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    modified_at         TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by          UUID                     NOT NULL,
    modified_by         UUID                     NOT NULL,
    workout_exercise_id UUID                     NOT NULL,
    set_number          INTEGER                  NOT NULL,
    reps                INTEGER,
    weight              DECIMAL(5, 2),
    rest_time           INTEGER,
    comment             VARCHAR(255),
    CONSTRAINT pk_workout_exercise_set PRIMARY KEY (id)
);

--changeset ValentynBondarenko:alter-table_workout_data_routine_exercise
ALTER TABLE routine_exercise
    ADD CONSTRAINT FK_ROUTINE_EXERCISE_ON_EXERCISE FOREIGN KEY (exercise_id) REFERENCES exercise (id);

--changeset ValentynBondarenko:alter-table_workout_data_routine_exercise_2
ALTER TABLE routine_exercise
    ADD CONSTRAINT FK_ROUTINE_EXERCISE_ON_ROUTINE FOREIGN KEY (routine_id) REFERENCES routine (id);

--changeset ValentynBondarenko:alter-table_workout_data_workout_exercise
ALTER TABLE workout_exercise
    ADD CONSTRAINT FK_WORKOUT_EXERCISE_ON_EXERCISE FOREIGN KEY (exercise_id) REFERENCES exercise (id);

--changeset ValentynBondarenko:alter-table_workout_data_workout_exercise_2
ALTER TABLE workout_exercise
    ADD CONSTRAINT FK_WORKOUT_EXERCISE_ON_WORKOUT FOREIGN KEY (workout_id) REFERENCES workout (id);

--changeset ValentynBondarenko:alter-table_workout_data_workout_exercise_set
ALTER TABLE workout_exercise_set
    ADD CONSTRAINT FK_WORKOUT_EXERCISE_SET_ON_WORKOUT_EXERCISE FOREIGN KEY (workout_exercise_id) REFERENCES workout_exercise (id);

--changeset ValentynBondarenko:alter-table_workout_data_workout
ALTER TABLE workout
    ADD CONSTRAINT FK_WORKOUT_ON_BASED_ON_WORKOUT FOREIGN KEY (based_on_workout_id) REFERENCES workout (id);

--changeset ValentynBondarenko:alter-table_workout_data_workout_2
ALTER TABLE workout
    ADD CONSTRAINT FK_WORKOUT_ON_ROUTINE FOREIGN KEY (routine_id) REFERENCES routine (id);
