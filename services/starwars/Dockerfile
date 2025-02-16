FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/starwars-1.0.jar starwars-1.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "starwars-1.0.jar", "--spring.config.location=file:/app/server-config.yml"]