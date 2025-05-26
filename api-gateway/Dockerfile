# Stage 1: Build application using Gradle
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

COPY . .

# Make sure gradlew is executable
RUN chmod +x ./gradlew

# Build using wrapper (always preferred)
RUN ./gradlew bootJar --no-daemon

# Stage 2: Final image
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]
