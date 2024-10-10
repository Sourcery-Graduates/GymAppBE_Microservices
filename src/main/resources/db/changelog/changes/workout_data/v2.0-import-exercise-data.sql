CREATE TABLE temp_exercise_data (
    exercise_data TEXT
);

COPY temp_exercise_data (exercise_data)
    FROM '/var/lib/postgresql/import-data/formatted_exercises.json'
    WITH (FORMAT text);

