# Stage 1: Build
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY . .

# Fix permission then build
RUN chmod +x gradlew && ./gradlew installDist --no-daemon

# Stage 2: Run
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/install/ktor-sample .

EXPOSE 8080

ENTRYPOINT ["bin/ktor-sample"]