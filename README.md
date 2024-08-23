# Bank Service Application

Demo final project. IT School, Germany, 2023-2024.
// FIXME add Telran, Telran It School
## Description

**Bank Service Application** - a backend-driven application that simulates core banking operations.
// FIXME replace hyphen with long dash, every symbol is important 

The application provides the following functionalities:

- Client management
- Manager management 
- Account management
- Card management
- Agreement management
- Product management
- Transaction processing

// FIXME I would rather write technology stackT
he system consists of:
- **[Backend]** developed using the Spring Boot Framework
- **[Database]** managed by PostgreSQL for persistent data storage
- **[Security layer]** implemented using Spring Security for authentication and authorization
- **[Containerization]** powered by Docker for smooth deployment and scalability

Additionally, the application architecture follows RESTful principles and supports security protocols including SSL and OAuth2 for secure communication.// FIXME also could be described in stack oin few words


## Technologies Used

- **Spring Boot 3.1.4**
- **PostgreSQL**
- **Docker**
- **Lombok**
- **MapStruct**
- **Spring Security** 
- **H2 Database**
- **JUnit 5**
- **Spring Boot Test**
- **MockMvc**
- **Mockito**


## Setup and Installation

### Prerequisites

- JDK 21
- Docker

### Getting Started

1. **Clone the repository:**

   ```bash
   git clone https://github.com/YehorShyshkin/BankServiceProject.git
   cd BankServiceProject
   
2. **Build the project using Maven:**
   ```bash
   ./mvn clean install
   
3. **Run the application:**
    ```bash
   ./mvn spring-boot:run
4. **Run Docker Compose to start PostgreSQL and PgAdmin:**
    ```bash
    docker-compose up
   // FIXNE why user should at first install and run? It must be all done in docker

Ensure that your docker-compose.yml file is correctly configured with the environment variables. // FXIME water

### Configuration
Update the `.env` file with your PostgreSQL and PgAdmin credentials: // FXIME also must be done in docker by yourself. Finally user must run only docker-compose up

SPRING_DATASOURCE_USER=your_postgres_user  
SPRING_DATASOURCE_PASSWORD=your_postgres_password  
PGADMIN_DEFAULT_EMAIL=your_pgadmin_email  
PGADMIN_DEFAULT_PASSWORD=your_pgadmin_password  

### Running Tests

To run the tests, use Maven:  // FXIME water, we consider that project will be explored by experts, they know for sure how to do it 

    mvn test

## Testing

![Jacoco Testing](screenshots/Jacoco%20Testing.png)
![Jacoco with Java.png](screenshots/Jacoco%20with%20Java.png)
### Overview

This project uses JUnit 5 and Spring Boot's testing framework to ensure the quality and functionality of the application. Tests cover various functionalities including CRUD operations, validation, and exception handling for different controllers.
// FXIME water, I'd say wich test I have, coverage, maybe libs, that all

### Account Controller Tests

#### 1. **Create Account**// FXIME this and below looks not related to tests bu more like rest api guide, also redundant, water

**Description**: Verifies that a new account is created successfully with the correct details.

- **Endpoint**: `POST /accounts/creates`
- **Expected Result**: Returns a 201 status with the created account details.

#### 2. **Get Account By ID**

**Description**: Checks if an existing account can be retrieved by its ID.

- **Endpoint**: `GET /accounts/find/{accountId}`
- **Expected Result**: Returns a 200 status with the account details.

#### 3. **Update Account**

**Description**: Tests updating an account's details and verifies that the updated account is returned.

- **Endpoint**: `GET /accounts/update/{accountId}`
- **Expected Result**: Returns a 200 status with the updated account details.

#### 4. **Soft Delete Account**

**Description**: Tests the soft deletion of an account by marking it as deleted.

- **Endpoint**: `GET /accounts/delete/{accountId}`
- **Expected Result**: Returns a 200 status indicating successful deletion with updated account details.

### Agreement Controller Tests

#### 1. **Create Agreement**

**Description**: Verifies that a new agreement is created successfully with the correct details.

- **Endpoint**: `POST /agreements/creates`
- **Expected Result**: Returns a 201 status with the created agreement details.

#### 2. **Get Agreement By ID**

**Description**: Checks if an existing agreement can be retrieved by its ID.

- **Endpoint**: `GET /agreements/find/{agreementId}`
- **Expected Result**: Returns a 200 status with the agreement details.

#### 3. **Update Agreement**

**Description**: Tests updating an agreement's details and verifies that the updated agreement is returned.

- **Endpoint**: `GET /agreements/update/{agreementId}`
- **Expected Result**: Returns a 200 status with the updated agreement details.

#### 4. **Exception Handling**

**Description**: Tests how the application handles exceptions such as when creating or finding an agreement with invalid data.

- **Endpoint**: Varies by specific exception scenarios.
- **Expected Result**: Returns appropriate HTTP status codes (e.g., 404 Not Found) and error messages.

### Card Controller Tests

#### 1. **Create Card**

**Description**: Verifies that a new card is created successfully with the correct details.

- **Endpoint**: `POST /cards/create`
- **Expected Result**: Returns a 201 status with the created card details.

#### 2. **Get Card By ID**

**Description**: Checks if an existing card can be retrieved by its ID.

- **Endpoint**: `GET /cards/find/{cardId}`
- **Expected Result**: Returns a 200 status with the card details.

#### 3. **Generate Card Number**

**Description**: Tests the generation of card numbers for different payment systems.

- **Endpoint**: Not applicable (utility test)
- **Expected Result**: Valid card numbers are generated and match expected formats.

#### 4. **Generate Card Expiration Date**

**Description**: Tests the generation of a valid card expiration date.

- **Endpoint**: Not applicable (utility test)
- **Expected Result**: Expiration date is valid (i.e., after today and within 10 years).

#### 5. **Generate Card CVV**

**Description**: Tests the generation of a valid card CVV.

- **Endpoint**: Not applicable (utility test)
- **Expected Result**: CVV is a valid 3-digit number.

#### 6. **Update Card**

**Description**: Tests updating a card's details and verifies that the updated card is returned.

- **Endpoint**: `GET /cards/update/{cardId}`
- **Expected Result**: Returns a 200 status with the updated card details.

#### 7. **Delete Card**

**Description**: Tests the deletion of a card.

- **Endpoint**: `GET /cards/delete/{cardId}`
- **Expected Result**: Returns a 200 status indicating successful deletion.

### Client Controller Tests

#### 1. **Create Client**

**Description**: Verifies that a new client is created successfully with the correct details.

- **Endpoint**: `POST /clients/creates`
- **Expected Result**: Returns a 201 status with the created client details.

#### 2. **Get Client By ID**

**Description**: Checks if an existing client can be retrieved by its ID.

- **Endpoint**: `GET /clients/find/{clientId}`
- **Expected Result**: Returns a 200 status with the client details.

#### 3. **Update Client**

**Description**: Tests updating a client's details and verifies that the updated client is returned.

- **Endpoint**: `GET /clients/update/{clientId}`
- **Expected Result**: Returns a 200 status with the updated client details.

#### 4. **Soft Delete Client**

**Description**: Tests the soft deletion of a client by marking it as deleted.

- **Endpoint**: `GET /clients/delete/{clientId}`
- **Expected Result**: Returns a 200 status indicating successful deletion with updated client details.

### Manager Controller Tests

#### 1. **Serialization and Deserialization**

**Description**: Verifies that a `ManagerDTO` object is correctly serialized to JSON and deserialized back to a `ManagerDTO` object.

- **Endpoint**: Not applicable (utility test)
- **Expected Result**: The original and deserialized `ManagerDTO` objects should be equivalent.

#### 2. **Manager to DTO Mapping**

**Description**: Tests the mapping between a `Manager` entity and `ManagerDTO`.

- **Endpoint**: Not applicable (utility test)
- **Expected Result**: The `ManagerDTO` should reflect the correct fields from the `Manager` entity.

#### 3. **Create Manager**

**Description**: Verifies that a new manager is created successfully with the correct details.

- **Endpoint**: `POST /managers/creates`
- **Expected Result**: Returns a 201 status with the created manager details.

#### 4. **Get Manager By ID**

**Description**: Checks if an existing manager can be retrieved by its ID.

- **Endpoint**: `GET /managers/find/{managerId}`
- **Expected Result**: Returns a 200 status with the manager details.

#### 5. **Update Manager**

**Description**: Tests updating a manager's details and verifies that the updated manager is returned.

- **Endpoint**: `GET /managers/update/{managerId}`
- **Expected Result**: Returns a 200 status with the updated manager details.

#### 6. **Soft Delete Manager**

**Description**: Tests the soft deletion of a manager by marking it as deleted.

- **Endpoint**: `GET /managers/delete/{managerId}`
- **Expected Result**: Returns a 200 status indicating successful deletion with updated manager details.

### Product Controller Tests

#### 1. **Create Product**

**Description**: Verifies that a new product is created successfully with the correct details.

- **Endpoint**: `POST /products/creates`
- **Expected Result**: Returns a 201 status with the created product details.

- **Test Details**:
    - **Test Case**: `testCreateProduct`
    - **Description**: This test creates a new `ProductDTO` object with specified details and checks if it is successfully created by comparing the returned product with the original one.

#### 2. **Get Product By ID**

**Description**: Checks if an existing product can be retrieved by its ID.

- **Endpoint**: `GET /products/find/{productId}`
- **Expected Result**: Returns a 200 status with the product details.

- **Test Details**:
    - **Test Case**: `testGetById`
    - **Description**: This test attempts to retrieve a product by its ID and compares the retrieved product with a predefined `ProductDTO` object to verify accuracy.

#### 3. **Update Product**

**Description**: Tests updating a product's details and verifies that the updated product is returned.

- **Endpoint**: `GET /products/update/{productId}`
- **Expected Result**: Returns a 200 status with the updated product details.

- **Test Details**:
    - **Test Case**: `testUpdateProduct`
    - **Description**: This test updates the details of an existing product and verifies that the updated product matches the given `ProductDTO` object.

#### 4. **Soft Delete Product**

**Description**: Tests the soft deletion of a product by marking it as deleted.

- **Endpoint**: `GET /products/delete/{productId}`
- **Expected Result**: Returns a 200 status indicating successful deletion with updated product details.

- **Test Details**:
    - **Test Case**: `testSoftDeleteProduct`
    - **Description**: This test soft deletes a product and checks if the product is marked as deleted correctly, verifying the updated product details.

### Transaction Controller Tests

#### 1. **Create Transaction**

**Description**: Verifies that a new transaction is created successfully with the correct details.

- **Endpoint**: `POST /transactions/create`
- **Expected Result**: Returns a 200 status with the created transaction details.

- **Test Details**:
    - **Test Case**: `testCreateTransaction`
    - **Description**: This test creates a new `TransactionDTO` object with specified details and checks if it is successfully created by comparing the returned transaction with the original one.

#### 2. **Get Transaction By ID**

**Description**: Checks if an existing transaction can be retrieved by its ID.

- **Endpoint**: `GET /transactions/find/{transactionId}`
- **Expected Result**: Returns a 200 status with the transaction details.

- **Test Details**:
    - **Test Case**: `testGetById`
    - **Description**: This test attempts to retrieve a transaction by its ID and compares the retrieved transaction with a predefined `TransactionDTO` object to verify accuracy.

#### 3. **Delete Transaction By ID**

**Description**: Tests the deletion of a transaction by marking it as deleted and verifies if the account balances are updated correctly.

- **Endpoint**: `GET /transactions/delete/{transactionId}`
- **Expected Result**: Returns a 200 status indicating successful deletion.

- **Test Details**:
    - **Test Case**: `testDeleteTransactionById`
    - **Description**: This test soft deletes a transaction and verifies if the transaction is marked as deleted and checks if the associated account balances are updated as expected.
// FXIME If you want have REST api description, better to do it in separate file, this is not for readme.md

