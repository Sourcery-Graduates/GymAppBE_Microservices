FROM postgres:14-alpine

ENV POSTGRES_DB=gym_app_db \
    POSTGRES_USER=gym_app_user \
    POSTGRES_PASSWORD=gym_app_pass

# Copy initialization scripts
RUN mkdir -p /var/lib/postgresql/import-data

COPY ./db/changelog/changes/workout_data/exercises_data/formatted_exercises.json /var/lib/postgresql/import-data/

RUN chmod -R 755 /docker-entrypoint-initdb.d

# Set the default entrypoint for the PostgreSQL container
ENTRYPOINT ["docker-entrypoint.sh"]

# Expose PostgreSQL's default port
EXPOSE 5432

# Default CMD to run PostgreSQL
CMD ["postgres"]

