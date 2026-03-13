# Stage 1: Build the project
FROM eclipse-temurin:21-jdk AS builder

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Build the project using Gradle
RUN ./gradlew installDist --no-daemon

# Stage 2: Run the app
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built app from the builder stage
COPY --from=builder /app/build/install/ktor-sample /app

# Set entrypoint
CMD ["bin/ktor-sample"]