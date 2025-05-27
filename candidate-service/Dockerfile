FROM gradle:8.5-jdk21 AS build-stage

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

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build-stage /app/build/libs/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]