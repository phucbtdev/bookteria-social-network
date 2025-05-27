FROM gradle:8.5-jdk21 AS builder

ARG USERNAME
ARG TOKEN
ENV USERNAME=${USERNAME}
ENV TOKEN=${TOKEN}

WORKDIR /app
COPY . .

RUN if [ -f "./gradlew" ]; then \
        chmod +x ./gradlew && ./gradlew bootJar --no-daemon; \
    else \
        gradle bootJar --no-daemon; \
    fi

FROM openjdk:21-jre-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]