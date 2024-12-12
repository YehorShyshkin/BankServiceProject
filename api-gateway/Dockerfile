FROM eclipse-temurin:21-jre-ubi9-minimal
COPY target/api-gateway-1.0-SNAPSHOT.jar app.jar
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "/app.jar"]