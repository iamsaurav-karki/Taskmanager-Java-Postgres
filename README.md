# Task Manager Application

A production-grade full-stack Java web application for task management built with Spring Boot and PostgreSQL.

## Features

- User registration and authentication
- Create, view, update, and delete tasks
- Task prioritization
- Task completion tracking
- Responsive design

## Technologies

- Java 17
- Spring Boot 3.1
- Spring Security
- Spring Data JPA
- PostgreSQL
- Thymeleaf
- Maven

## Running Locally

### Prerequisites

- JDK 17+
- Maven
- PostgreSQL

### Setup

1. Clone the repository
2. Create a PostgreSQL database and user:
   ```sql
   CREATE DATABASE taskmanager;
   CREATE USER taskapp WITH ENCRYPTED PASSWORD 'securepassword';
   GRANT ALL PRIVILEGES ON DATABASE taskmanager TO taskapp;
   ```
3. Update `application.properties` if needed with your database credentials
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
5. Access the application at http://localhost:8080

## Production Deployment

### Docker

Build and run with Docker:

```bash
mvn clean package
docker build -t taskmanager .
docker run -p 8080:8080 taskmanager
```

### Environment Variables

Set these environment variables for production:

- `JDBC_DATABASE_URL`: PostgreSQL connection URL
- `JDBC_DATABASE_USERNAME`: Database username
- `JDBC_DATABASE_PASSWORD`: Database password
- `SPRING_PROFILES_ACTIVE=prod`: Enable production profile

## License

MIT


