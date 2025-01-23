# GymApp Backend

A Spring Boot-based backend application for managing gym workouts, exercises, and routines. This application provides a comprehensive API for tracking fitness progress and managing workout plans.

## Technology Stack

### Core
- Java 21
- Spring Boot 3.3.4
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL
- Liquibase for database migrations

### Testing
- JUnit 5
- Testcontainers
- Spring Boot Test

### Documentation & Development
- OpenAPI (Swagger)
- Spring Actuator
- Docker & Docker Compose

## Project Structure

The application follows a modular architecture with the following main components:
```
src/
├── main/
│ ├── java/
│ │ └── com/sourcery/gymapp/backend/
│ │ ├── workout/ # Workout management module
│ │ ├── userauth/ # Authentication module
│ │ ├── userprofile/ # User profiles module
│ │ └── sharedlinks/ # Sharing functionality
│ └── resources/
│ ├── db/changelog/ # Database migrations
│ └── application.yaml # Application configuration
```

## Getting Started

### Prerequisites
- JDK 21
- Docker and Docker Compose
- PostgreSQL (if running locally)

### Running with Docker

1. Clone the repository:
```bash
git clone https://github.com/Sourcery-Graduates/GymAppBE.git
cd GymAppBE
```

2. Start the application using Docker Compose:
```bash
docker-compose up -d
```

The application will be available at `http://localhost:8080`

### Running Locally

1. Start PostgreSQL database:
```bash
docker-compose up -d postgres
```

2. Run the application:
```bash
./gradlew bootRun
```

## API Documentation

The API documentation is available through Swagger UI:
- URL: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## Testing

Run unit tests:
```bash
./gradlew test
```

Run integration tests:
```bash
./gradlew integrationTest
```

## Database Schema

The application uses multiple schemas for different modules:
- `public` - Common tables
- `user_auth` - Authentication and authorization
- `workout_data` - Workout-related data
- `user_profiles` - User profile information
- `shared_links` - Sharing functionality

## Contributing

### Branch Naming Convention

All branches should follow the Jira ticket naming pattern:
```
GYM-{ticket-number}-{short-description}
```

Examples:
- `GYM-102-error-handling-user-already-exists`
- `GYM-114-delete-routine-with-exercises`
- `GYM-122-add-integration-tests`

### Development Workflow

1. Pick up a ticket from Jira board
2. Create a new branch following the naming convention:
   ```bash
   git checkout -b GYM-{ticket-number}-{description}
   ```
3. Make your changes
4. Commit your changes using conventional commits:
   ```bash
   git commit -m "GYM-{ticket-number}: feature description"
   ```
5. Push to the branch:
   ```bash
   git push origin GYM-{ticket-number}-{description}
   ```
6. Create a Pull Request with the following:
   - Link to Jira ticket
   - Brief description of changes
   - Any additional notes for reviewers

### Pull Request Requirements

- All tests must pass
- Code must follow project's style guide
- Changes must be reviewed by at least one team member
- Jira ticket must be linked
- Branch should be up to date with main

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

Copyright (c) 2024 Sourcery Academy Graduates
