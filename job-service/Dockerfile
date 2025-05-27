# Stage 1: Build ứng dụng với Gradle
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

# Copy gradle wrapper và config files trước
COPY gradlew* ./
COPY gradle/ ./gradle/

# Copy build files
COPY build.gradle* ./
COPY settings.gradle* ./

# Copy source code
COPY src/ ./src/

# Make sure gradlew is executable
RUN chmod +x ./gradlew

# Build using wrapper
RUN ./gradlew bootJar --no-daemon

# Stage 2: Chạy ứng dụng với JDK
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy jar file từ build stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port (thay đổi port tùy theo service)
EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]