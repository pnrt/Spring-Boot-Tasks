# Use a lightweight JDK base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy Gradle build output (JAR file) into the container
COPY build/libs/tasks-0.0.1-SNAPSHOT.jar app.jar

# Expose the port used by the Spring Boot app
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]