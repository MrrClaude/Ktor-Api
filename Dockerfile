FROM gradle:8.4.0-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle installDist

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/build/install/Ktor-Api /app

CMD ["./bin/ktor-sample"]