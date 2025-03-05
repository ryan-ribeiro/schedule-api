localhost:8080/swagger-ui/# Task Scheduling API

&#x20;  &#x20;

This project is an API built using **Java, Spring Boot, Hibernate, Spring Data JPA, Spring Security, Swagger, HATEOAS, PostgreSQL**, and implements **Bearer Authentication** and **ISO 8601** date formats. The project supports **HTTP PATCH**, includes **pagination**, and follows all four levels of **Richardson Maturity Model** required to be RESTful.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [Database](#database)
- [Contributing](#contributing)

## Installation

1. Clone the repository:

```bash
git clone https://github.com/ryan-ribeiro/schedule-api.git
```

2. Install dependencies with Maven:

```bash
mvn install
```

3. Install [PostgreSQL](https://www.postgresql.org/) and create a database.

4. Configure the database connection in `application.properties`:

```properties
spring.application.name=schedule-api
spring.datasource.url=jdbc:postgresql://localhost:5432/schedule-api
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
```

## Usage

1. Start the application with Maven:

```bash
mvn spring-boot:run
```

2. The API will be accessible at
```localhost:8080```
Swagger is available at
```localhost:8080/swagger-ui/index.html#/```
   

## API Endpoints

The API provides the following endpoints:

**Authentication:**
```markdown
POST /auth/register - Register a new user (no authentication required)

POST /auth/login - Authenticate an existing user, returns a token (no authentication required)

GET /auth/test - Returns a status 200 ok if the user is authenticated (authentication required)

GET /auth/test/customer - Returns a status 200 ok if the user has the role "ROLE_CUSTOMER" (ROLE_CUSTOMER authentication required)

GET /auth/test/administrator - Returns a status 200 ok if the user has the role "ROLE_ADMINISTRATOR" (ROLE_ADMINISTRATOR authentication required)
```

**Tasks:**
```markdown
GET /task - Retrieve a list of all tasks in the database (pagination available, ROLE_ADMINISTRATOR authentication required)

GET /task/user/{username} - Retrieve a list of all tasks made by the user "username", but only if you're the "username" user authenticated. (ROLE_CUSTOMER authentication required)

GET /task/{id} - Retrieve a task which id is "{id}" made by the user "username", but only if you're the "username" user authenticated (pagination available, ROLE_CUSTOMER authentication required).

POST /task - Create a new task in the name of the user "username", but only if you're the "username" user authenticated (ROLE_CUSTOMER authentication required)

PUT /task/{id} - Update all of an existing task's property in the name of the user "username", but only if you're the "username" user authenticated (ROLE_CUSTOMER authentication required)

PATCH /task/{id} - Update an existing task's property in the name of the user "username", but only if you're the "username" user authenticated (ROLE_CUSTOMER authentication required)

DELETE /task/{id} - Delete an existing task in the name of the user "username", but only if you're the "username" user authenticated (ROLE_CUSTOMER authentication required)

```
**User:**
```markdown
GET /user - Retrieve a list of all users in the database (pagination available, ROLE_ADMINISTRATOR authentication required)

GET /user/{id} - Retrieve a list of all tasks made by the user "username" and his info, but only if you're the "username" user authenticated. (pagination available, ROLE_CUSTOMER authentication required)

PUT /user/{id} - Update all of an existing user's property in the name of the user "username", but only if you're the "username" user authenticated (ROLE_CUSTOMER authentication required)

PATCH /user/{id} - Update an existing user's property in the name of the user "username", but only if you're the "username" user authenticated (ROLE_CUSTOMER authentication required)

DELETE /user/{id} - Delete an existing user in the name of the user "username", but only if you're the "username" user authenticated (ROLE_CUSTOMER authentication required)

```

## Authentication

The API uses **Spring Security** for authentication, with **Bearer Token** authentication. The following roles are available:

```
CUSTOMER -> Standard role for authenticated users.
ADMINISTRATOR -> Admin role for accessing all the content persisted, but not to delete or update anything.
```

To access protected endpoints, include the Bearer token in the Authorization header:

```
Authorization: Bearer <your-token>
```

If you are using Swagger, you also need to authenticate.

## Database

The project utilizes [PostgreSQL](https://www.postgresql.org/) as the database.

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request to the repository.

When contributing to this project, please follow the existing code style, [commit conventions](https://www.conventionalcommits.org/en/v1.0.0/), and submit your changes in a separate branch.

