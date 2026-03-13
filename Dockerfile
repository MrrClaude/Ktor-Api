# ---------- Stage 1: Build ----------
FROM gradle:8.12-jdk17 AS builder

# Set working directory inside the container
WORKDIR /app

# Copy project files
COPY . .

# Build the project
RUN gradle installDist --no-daemon

# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:17-jdk

# Set working directory inside the runtime container
WORKDIR /app

# Copy the built application from the builder stage
COPY --from=builder /app/build/install /app

# Expose Ktor default port
EXPOSE 8080

# Run the application
CMD ["bin/ktor-sample"]