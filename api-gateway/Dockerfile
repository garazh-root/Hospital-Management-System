FROM eclipse-temurin:22-jdk AS builder

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

RUN ./gradlew build --no-daemon || true

COPY . .

RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:22-jdk AS runner

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 1013

ENTRYPOINT ["java", "-jar", "app.jar"]

