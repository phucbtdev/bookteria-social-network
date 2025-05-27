# Stage 1: Build ứng dụng với Gradle
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

# Copy tất cả files (vì đã set directory context)
COPY . .

# Make sure gradlew is executable (nếu có)
RUN if [ -f "./gradlew" ]; then chmod +x ./gradlew && ./gradlew bootJar --no-daemon; else gradle bootJar --no-daemon; fi

# Stage 2: Chạy ứng dụng với JDK
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy jar file từ build stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port (thay đổi port tùy theo service)
EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]