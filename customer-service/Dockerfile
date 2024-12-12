FROM eclipse-temurin:21-jre-ubi9-minimal
COPY target/customer-service-1.0-SNAPSHOT.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "/app.jar"]