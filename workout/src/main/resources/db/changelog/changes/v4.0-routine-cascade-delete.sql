--liquibase formatted sql

--changeset TymofiiSkrypko:drop-old-constraint-fk_routine_exercise_on_routine
ALTER TABLE routine_exercise
    DROP CONSTRAINT FK_ROUTINE_EXERCISE_ON_ROUTINE;

--changeset TymofiiSkrypko:drop-old-constraint-fk_workout_on_routine
ALTER TABLE workout
    DROP CONSTRAINT FK_WORKOUT_ON_ROUTINE;

--changeset TymofiiSkrypko:create-new-constraint-fk_routine_exercise_on_routine-with-delete-cascade
ALTER TABLE routine_exercise
    ADD CONSTRAINT FK_ROUTINE_EXERCISE_ON_ROUTINE
        FOREIGN KEY (routine_id) REFERENCES routine (id)
            ON DELETE CASCADE;

--changeset TymofiiSkrypko:create-new-constraint-fk_workout_on_routine-with-delete-cascade
ALTER TABLE workout
    ADD CONSTRAINT FK_WORKOUT_ON_ROUTINE
        FOREIGN KEY (routine_id) REFERENCES routine (id)
            ON DELETE SET NULL;
