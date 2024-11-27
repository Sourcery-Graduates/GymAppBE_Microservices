FROM gradle:8.5-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon

FROM openjdk:21-slim AS production
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/gymapp-backend-0.0.1-SNAPSHOT.jar /app/gymapp-backend-0.0.1-SNAPSHOT.jar
COPY --from=build /home/gradle/src/src/main/resources /app/resources
COPY ./src/main/resources/db/changelog/changes/workout_data/exercises_data /app/import-data
ENTRYPOINT ["java","-jar","app/gymapp-backend-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=deployment"]