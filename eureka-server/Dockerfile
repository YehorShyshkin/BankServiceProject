FROM eclipse-temurin:21-jre-ubi9-minimal
COPY target/eureka-server-1.0-SNAPSHOT.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app.jar"]


#FROM eclipse-temurin:21-jre-jammy
#
## Use the official OpenJDK image as the base image
#FROM maven:3.9.5-eclipse-temurin-21 AS build
#
#
#
## Set the working directory inside the container
#WORKDIR /app
#
## Copy the entire project (including parent POM and modules)
#COPY . /app
#
## Build the project and create the JAR file
#RUN mvn clean package -DskipTests
#
## Second stage: Use a smaller image for running the application
#FROM eclipse-temurin:21-jre-jammy
#
## Set the working directory inside the container
#WORKDIR /app
#
## Copy the JAR file from the build stage
##COPY --from=build /app/eureka-server/target/*.jar app.jar
#COPY --from=build /app/target/eureka-server.jar /app/eureka-server.jar
#
## Expose the Eureka server's default port
#EXPOSE 8761
#
## Define the entry point for the container
#ENTRYPOINT ["java", "-jar", "/app/eureka-server.jar"]