FROM eclipse-temurin:21-jre-ubi9-minimal
COPY target/manager-service-1.0-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]