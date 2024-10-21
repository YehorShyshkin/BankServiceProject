# Use the official OpenJDK image as the base image
FROM openjdk:21-jdk-slim AS build

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project (including parent POM and modules)
COPY . .

# Go offline to download dependencies for all modules
RUN mvn -pl manager-service dependency:go-offline

# Build the project and skip tests for the Docker image
RUN mvn -pl manager-service clean package -DskipTests

# Second stage: Use a smaller image for running the application
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/manager-service/target/*.jar app.jar

# Expose the application port
EXPOSE 443

# Define the entry point for the container
ENTRYPOINT ["java", "-jar", "app.jar"]