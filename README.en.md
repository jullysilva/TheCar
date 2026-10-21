# TheCar

[🇧🇷 Português](README.md) | 🇺🇸 English

Car rental platform ("Airbnb for cars") built with **Spring Boot**. It offers a Thymeleaf web interface to manage the car catalog and a REST API secured with **JWT**.

## Stack

- Java 21, Spring Boot, Maven
- Spring Web, Spring Data JPA, Spring Security
- JWT (jjwt)
- Thymeleaf + Thymeleaf Layout Dialect
- H2 Database (in-memory)
- springdoc-openapi (Swagger UI)
- Lombok

## Structure

```
src/main/java/com/example/thecar/
├── config/       # Swagger configuration
├── controller/   # Web (Thymeleaf) and REST controllers
├── model/        # Car and User entities
├── repository/   # JPA repositories
└── security/     # JWT filter, token service and SecurityConfig
src/main/resources/templates/  # Views: index, novo, editar, login, register
```

## Routes

### Web interface

| Method | Route | Description |
|---|---|---|
| GET | `/login` | Login page |
| GET | `/cars` | Car list |
| POST | `/cars/add` | Adds a car |
| GET | `/cars/delete/{id}` | Deletes a car |

### REST API

| Method | Route | Description |
|---|---|---|
| POST | `/api/auth/register` | Creates a user |
| POST | `/api/auth/login` | Authenticates and returns a JWT |
| GET | `/api/cars` | Lists cars |
| GET | `/api/cars/{id}` | Gets a car |
| POST | `/api/cars` | Creates a car |
| PUT | `/api/cars/{id}` | Updates a car |
| DELETE | `/api/cars/{id}` | Deletes a car |

Send the token in the `Authorization: Bearer <token>` header on protected routes.

## Getting started

Requirement: **JDK 21**.

```bash
./mvnw spring-boot:run
```

The app runs at `http://localhost:8080`.

| Resource | URL |
|---|---|
| Web app | `http://localhost:8080/cars` |
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| H2 console | `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:carrosdb`, user `sa`) |

To run the tests:

```bash
./mvnw test
```

## Known issues

`SecurityConfig` currently requires authentication on `/api/auth/**`, Swagger UI and the H2 console, and there is no `GET /register` handler. The fix is tracked in [#2](https://github.com/jullysilva/TheCar/issues/2).
