# Stage 1: Build application using Gradle
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

# Copy everything into the container
COPY . .

# Build the application using Gradle
RUN gradle bootJar --no-daemon

# Stage 2: Create final image
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]
