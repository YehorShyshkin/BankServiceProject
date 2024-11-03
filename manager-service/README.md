### Manager Service

The **Manager Service** application provides a RESTful API for managing managers' data, 
including creating, retrieving, updating, and deleting manager records.
It also integrates Micrometer metrics for monitoring and validation for input fields.
The project is built with **Spring Boot**, **Hibernate**, **PostgreSQL**, and **Micrometer**.

#### Table of Contents
- [Setup](#setup)
- [Project Structure](#project-structure)
- [Annotations and Logging](#annotations-and-logging)
- [Endpoints](#endpoints)
- [Validation](#validation)
- [Exception Handling](#exception-handling)
- [Metrics and Monitoring](#metrics-and-monitoring)
- [Testing](#testing)
- [Database Operations](#database-operations)

#### Setup
1. **Database Configuration**:
    - The project is configured for **PostgreSQL** database. Database connection details should be set via environment variables:
        - `SPRING_DATASOURCE_URL`
        - `SPRING_DATASOURCE_USER`
        - `SPRING_DATASOURCE_PASSWORD`
2. **Start the Application**:
    - Run the service using Spring Boot on port `8081`
    - The application automatically exposes metrics and health endpoints for Prometheus and other monitoring tools
3. **Dependencies**:
    - **Spring Boot** for REST and validation
    - **Hibernate** for ORM and JPA
    - **Micrometer** for metrics
    - **Lombok** for code reduction
    - **Jakarta Validation** for field validation
    - **Testcontainers** for integration testing

#### Project Structure
- **Configuration**:
    - Custom annotation for logging and monitoring method calls
    - Aspect for handling logging annotations
    - Test containers configuration for integration tests
- **Controllers**: REST endpoints for managing manager records
- **DTOs**: For manager creation, updating, find and deleted
- **Exceptions**: Custom exception handling
- **Model**: JPA entity for `Manager`
- **Service**: Business logic for managing records
- **Resources**:
    - Configuration file for database, server, and management settings

#### Annotations and Logging
Service uses custom annotation `@LogInfo` for automatic method monitoring.

For each annotated method it provides:
- Call counter
- Execution time measurement
- Automatic logging

Example log output:
```
INFO: Method: operation_name, Total Calls: 100, Average Execution Time (seconds): 0.23
```

Available metrics in Prometheus format:
- `{name}_count` - number of method calls
- `{name}_timer` - execution time stats
- `{name}_avg_time` - average execution time

If `name` parameter is not specified, method name will be used as metric name.

#### Endpoints
The service exposes the following endpoints:

| Endpoint                | HTTP Method | Description                       |
|-------------------------|-------------|-----------------------------------|
| `/managers`             | `POST`      | Creates a new manager             |
| `/managers/{id}`        | `GET`       | Retrieves a manager by ID         |
| `/managers/{id}`        | `PUT`       | Updates an existing manager       |
| `/managers/{id}`        | `DELETE`    | Deletes a manager by ID           |

#### Validation
- The application uses Jakarta Validation annotations to ensure input fields are correctly formatted and required fields are present

#### Exception Handling
- Custom exceptions are handled gracefully to provide meaningful error messages for various scenarios, such as not finding a manager or validation errors

#### Metrics and Monitoring
- Micrometer is used to track service metrics, such as total counts of create, update, find, and delete operations, as well as execution times for each operation
- Metrics are exposed on the `/actuator/metrics` endpoint, compatible with Prometheus for monitoring purposes

#### Testing

![Test manager-service.png](screenshots/Test%20manager-service.png)

##### Integration Tests
Service uses Testcontainers for integration testing.

Features:
- Automatically starts PostgreSQL container
- Configures test database properties
- Provides clean test environment

#### Database Operations

### DBUtil Component
Utility class for direct database operations.