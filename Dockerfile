FROM gradle:8.4.0-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle installDist

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/install/ktor-api /app

CMD ["./bin/ktor-api"]