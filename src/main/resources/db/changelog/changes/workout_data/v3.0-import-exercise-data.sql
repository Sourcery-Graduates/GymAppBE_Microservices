CREATE TABLE temp_exercise_data (
    exercise_data TEXT
);

COPY temp_exercise_data (exercise_data)
    FROM '/var/lib/postgresql/import-data/formatted_exercises.json'
    WITH (FORMAT text);


INSERT INTO workout_data.exercise (
    id, created_at, modified_at, created_by, modified_by, name, force, level, mechanic, equipment, description, category, primary_muscles, secondary_muscles, images
)
SELECT
    uuid_generate_v4(),  -- Generating new UUID for 'id'
    NOW(),                -- Setting 'created_at' to current time
    NOW(),                -- Setting 'modified_at' to current time
    uuid_generate_v4(),   -- Generating new UUID for 'created_by'
    uuid_generate_v4(),   -- Generating new UUID for 'modified_by'
    exercise_data::jsonb->>'name',           -- Extracting 'name' from JSON
    exercise_data::jsonb->>'force',          -- Extracting 'force' from JSON
    exercise_data::jsonb->>'level',          -- Extracting 'level' from JSON
    exercise_data::jsonb->>'mechanic',       -- Extracting 'mechanic' from JSON
    exercise_data::jsonb->>'equipment',      -- Extracting 'equipment' from JSON
    exercise_data::jsonb->>'description',    -- Extracting 'description' from JSON
    exercise_data::jsonb->>'category',       -- Extracting 'category' from JSON
    -- Converting 'primaryMuscles' JSON array to PostgreSQL text[]
    array(
        SELECT jsonb_array_elements_text(exercise_data::jsonb->'primaryMuscles')
    ),
    -- Converting 'secondaryMuscles' JSON array to PostgreSQL text[]
    array(
        SELECT jsonb_array_elements_text(exercise_data::jsonb->'secondaryMuscles')
    ),
    -- Converting 'images' JSON array to PostgreSQL text[]
    array(
        SELECT jsonb_array_elements_text(exercise_data::jsonb->'images')
    )
FROM temp_exercise_data;


DROP TABLE temp_exercise_data;
