<<<<<<< HEAD
# Bank Service Application

Demo final project. IT School, Germany, 2023-2024.

## Description

**Bank Service Application** - a backend-driven application that simulates core banking operations.

The application provides the following functionalities:

- Client management
- Manager management
- Account management
- Card management
- Agreement management
- Product management
- Transaction processing

The system consists of:
- **[Backend]** developed using the Spring Boot Framework
- **[Database]** managed by PostgreSQL for persistent data storage
- **[Security layer]** implemented using Spring Security for authentication and authorization
- **[Containerization]** powered by Docker for smooth deployment and scalability

Additionally, the application architecture follows RESTful principles and supports security protocols including SSL and OAuth2 for secure communication.

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
   

Ensure that your docker-compose.yml file is correctly configured with the environment variables.

### Configuration
Update the `.env` file with your PostgreSQL and PgAdmin credentials:

SPRING_DATASOURCE_USER=your_postgres_user  
SPRING_DATASOURCE_PASSWORD=your_postgres_password  
PGADMIN_DEFAULT_EMAIL=your_pgadmin_email  
PGADMIN_DEFAULT_PASSWORD=your_pgadmin_password  

### Running Tests

To run the tests, use Maven:   

    mvn test

## Testing

![Jacoco Testing](screenshots/Jacoco%20Testing.png)
![Jacoco with Java.png](screenshots/Jacoco%20with%20Java.png)
### Overview

This project uses JUnit 5 and Spring Boot's testing framework to ensure the quality and functionality of the application. Tests cover various functionalities including CRUD operations, validation, and exception handling for different controllers.

### Account Controller Tests

#### 1. **Create Account**

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


=======
# BankServiceProject



## Getting started

To make it easy for you to get started with GitLab, here's a list of recommended next steps.

Already a pro? Just edit this README.md and make it your own. Want to make it easy? [Use the template at the bottom](#editing-this-readme)!

## Add your files

- [ ] [Create](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#create-a-file) or [upload](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#upload-a-file) files
- [ ] [Add files using the command line](https://docs.gitlab.com/ee/gitlab-basics/add-file.html#add-a-file-using-the-command-line) or push an existing Git repository with the following command:

```
cd existing_repo
git remote add origin https://gitlab.com/GreamcAtch/bankservice.git
git branch -M main
git push -uf origin main
```

## Integrate with your tools

- [ ] [Set up project integrations](https://gitlab.com/GreamcAtch/bankservice/-/settings/integrations)

## Collaborate with your team

- [ ] [Invite team members and collaborators](https://docs.gitlab.com/ee/user/project/members/)
- [ ] [Create a new merge request](https://docs.gitlab.com/ee/user/project/merge_requests/creating_merge_requests.html)
- [ ] [Automatically close issues from merge requests](https://docs.gitlab.com/ee/user/project/issues/managing_issues.html#closing-issues-automatically)
- [ ] [Enable merge request approvals](https://docs.gitlab.com/ee/user/project/merge_requests/approvals/)
- [ ] [Set auto-merge](https://docs.gitlab.com/ee/user/project/merge_requests/merge_when_pipeline_succeeds.html)

## Test and Deploy

Use the built-in continuous integration in GitLab.

- [ ] [Get started with GitLab CI/CD](https://docs.gitlab.com/ee/ci/quick_start/index.html)
- [ ] [Analyze your code for known vulnerabilities with Static Application Security Testing (SAST)](https://docs.gitlab.com/ee/user/application_security/sast/)
- [ ] [Deploy to Kubernetes, Amazon EC2, or Amazon ECS using Auto Deploy](https://docs.gitlab.com/ee/topics/autodevops/requirements.html)
- [ ] [Use pull-based deployments for improved Kubernetes management](https://docs.gitlab.com/ee/user/clusters/agent/)
- [ ] [Set up protected environments](https://docs.gitlab.com/ee/ci/environments/protected_environments.html)

***

# Editing this README

When you're ready to make this README your own, just edit this file and use the handy template below (or feel free to structure it however you want - this is just a starting point!). Thanks to [makeareadme.com](https://www.makeareadme.com/) for this template.

## Suggestions for a good README

Every project is different, so consider which of these sections apply to yours. The sections used in the template are suggestions for most open source projects. Also keep in mind that while a README can be too long and detailed, too long is better than too short. If you think your README is too long, consider utilizing another form of documentation rather than cutting out information.

## Name
Choose a self-explaining name for your project.

## Description
Let people know what your project can do specifically. Provide context and add a link to any reference visitors might be unfamiliar with. A list of Features or a Background subsection can also be added here. If there are alternatives to your project, this is a good place to list differentiating factors.

## Badges
On some READMEs, you may see small images that convey metadata, such as whether or not all the tests are passing for the project. You can use Shields to add some to your README. Many services also have instructions for adding a badge.

## Visuals
Depending on what you are making, it can be a good idea to include screenshots or even a video (you'll frequently see GIFs rather than actual videos). Tools like ttygif can help, but check out Asciinema for a more sophisticated method.

## Installation
Within a particular ecosystem, there may be a common way of installing things, such as using Yarn, NuGet, or Homebrew. However, consider the possibility that whoever is reading your README is a novice and would like more guidance. Listing specific steps helps remove ambiguity and gets people to using your project as quickly as possible. If it only runs in a specific context like a particular programming language version or operating system or has dependencies that have to be installed manually, also add a Requirements subsection.

## Usage
Use examples liberally, and show the expected output if you can. It's helpful to have inline the smallest example of usage that you can demonstrate, while providing links to more sophisticated examples if they are too long to reasonably include in the README.

## Support
Tell people where they can go to for help. It can be any combination of an issue tracker, a chat room, an email address, etc.

## Roadmap
If you have ideas for releases in the future, it is a good idea to list them in the README.

## Contributing
State if you are open to contributions and what your requirements are for accepting them.

For people who want to make changes to your project, it's helpful to have some documentation on how to get started. Perhaps there is a script that they should run or some environment variables that they need to set. Make these steps explicit. These instructions could also be useful to your future self.

You can also document commands to lint the code or run tests. These steps help to ensure high code quality and reduce the likelihood that the changes inadvertently break something. Having instructions for running tests is especially helpful if it requires external setup, such as starting a Selenium server for testing in a browser.

## Authors and acknowledgment
Show your appreciation to those who have contributed to the project.

## License
For open source projects, say how it is licensed.

## Project status
If you have run out of energy or time for your project, put a note at the top of the README saying that development has slowed down or stopped completely. Someone may choose to fork your project or volunteer to step in as a maintainer or owner, allowing your project to keep going. You can also make an explicit request for maintainers.
>>>>>>> 50707d7739e2e64653b04a45757e2a557e31bcf8
