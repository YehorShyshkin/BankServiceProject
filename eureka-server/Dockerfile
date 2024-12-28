FROM eclipse-temurin:21-jre-ubi9-minimal
COPY target/eureka-server-1.0-SNAPSHOT.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app.jar"]