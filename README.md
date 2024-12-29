### Bank Service Application

Bank Service Application - simulating core banking operations through a microservices architecture.

Germany, 2023-2024.

##### System Architecture / Microservices Overview

- ##### Api-Gateway
    Entry point for all client requests

- ##### Auth-Service
    Handles authentication and authorization

- ##### Eureka-Server
    Service discovery and registration

- ##### Customer-Service
    Customer functions handled

- ##### Manager-Service
    Management functions handled

  | Category                | Core Components                                                          |
  |-------------------------|-------------------------------------------------------------------------|
  | **Backend Infrastructure** | Spring Boot 3.1.4  <br/>RESTful API  <br/>PostgreSQL  <br/>OAuth2     |
  | **DevOps & Deployment**  | Docker and Docker-Compose <br/> CI/CD <br/> Prometheus                                      |
  | **Testing Suite**        | JUnit 5 <br/> Spring Boot Test;<br/> MockMvc <br/>Mockito <br/> Testcontainers <br/> WireMock |
  | **Development Tools**    | Lombok <br/> MapStruct <br/> PostgreSQL                                                     |
  | **Security Features**    | Spring Security framework <br/> OAuth2 <br/> Role-based access control       |

  | Service          | Port |
  |------------------|------|
  | Api-Gateway      | 9000 |
  | Auth-Service     | 8084 |
  | Eureka-Server    | 8761 |
  | Customer-Service | 8082 |
  | Manager-Service  | 8081 |

##### CI/CD
![CI.CD.jpg](screenshots/CI.CD.jpg)

#### Docker

##### Build all dockers
![Build all dockers.png](screenshots/Build%20all%20dockers.png)

##### Docker all service
![Docker all service.png](screenshots/Docker%20all%20service.png)

#### Testing

##### Auth-service test
![auth-service test.png](auth-service/screenshots/auth-service%20test.png)

##### Manager-service test
![manager-service test.png](manager-service/screenshots/manager-service%20test.png)

##### Customer-service test
![customer-service test.png](customer-service/screenshots/customer-service%20test.png)

#### Create Users

##### Admin create

curl -i -X POST http://localhost:8084/users/create \                                                             
-H "Content-Type: application/json" \  
-d '{  
"email": "user@example.com",  
"password": "Password2&",  
"roleName": "ADMIN"  
}'  
  
![Admin create.png](auth-service/screenshots/Admin%20create.png)

##### Customer create

curl -i -X POST http://localhost:8082/customers \                                                 
-H "Content-Type: application/json" \  
-H "Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJleGFtcGxlLm1hbmFnZXJAZW1haWwuY29tIiwicm9sZSI6Ik1BTkFHRVIiLCJleHAiOjE3MzQwOTk4Mzl9.TKUrZViWuBP3kg-F_-JQJVRkkLjimL8uAlFs4WUcnuFhnABiqg9aGTc8ZP72yet8" \  
-d '{  
"firstName": "Emily",  
"lastName": "Taylor",  
"email": "emily.taylor.new@example.com",  
"password": "UniquePassword789!",  
"phoneNumber": "+1998765432",  
"taxNumber": "5678901234",  
"address": "321 Pine Boulevard",  
"city": "Dreamcity",  
"zipCode": "98765",  
"country": "UK"  
}'  

![Customer create.png](customer-service/screenshots/Customer%20create.png)

##### Manager create

curl -i -X POST http://localhost:8081/managers \  
-H "Content-Type: application/json" \  
-H "Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwicm9sZSI6IkFETUlOIiwiZXhwIjoxNzM1MjE3NjAwfQ.xvlwb02pf_AAVxQXZu48c9QQkI-bGvx3Ifrlxb0ccb5BbJqPYkgYQh1ahe4StZZ4" \  
-d '{  
"firstName": "Michael",  
"lastName": "Johnson",  
"email": "michael.johnson@example.com",  
"phoneNumber": "+1122334455",  
"password": "SecurePassword123!"  
}'    
  
![Manager create.png](manager-service/screenshots/Manager%20create.png)

##### Token
EMAIL="user@example.com"   
curl -i -X POST http://localhost:8084/auth/login \                                          
-H "Content-Type: application/json" \    
-d "{\"email\":\"${EMAIL}\",\"password\":\"Password2&\"}" 
  
![manager token.png](auth-service/screenshots/manager%20token.png)


##### Setup and Installation

##### Prerequisites

- JDK 21
- Docker
- Docker Compose

##### Getting Started

1. **Clone the repository:**

   ```bash
   git clone https://github.com/YehorShyshkin/BankServiceProject.git
   cd BankServiceProject

2. **Build the project using Maven:**
   ```bash
   ./mvn clean install

3. **Run Docker Compose to start PostgreSQL and PgAdmin:**
    ```bash
    docker-compose up

Ensure that your docker-compose.yml file is correctly configured with the environment variables.

### Running Tests

To run the tests, use Maven:

    mvn test

### Configuration
Update the `.env` file with your PostgreSQL and PgAdmin credentials:

SPRING_DATASOURCE_USER=your_postgres_user  
SPRING_DATASOURCE_PASSWORD=your_postgres_password  
PGADMIN_DEFAULT_EMAIL=your_pgadmin_email  
PGADMIN_DEFAULT_PASSWORD=your_pgadmin_password