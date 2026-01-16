FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the Maven wrapper and all source files
COPY . .

# Make sure mvnw is executable
RUN chmod +x mvnw

# Install DevTools and compile the project but DO NOT package it into a JAR
RUN ./mvnw install -DskipTests

# Expose the port used by the app
EXPOSE 8081

# Run in development mode with DevTools enabled (class hot reload)
CMD ["./mvnw", "spring-boot:run"]