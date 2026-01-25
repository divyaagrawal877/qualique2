# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests -B

# Production stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Create uploads directory with proper permissions (before switching user)
RUN mkdir -p uploads && chown spring:spring uploads

# Switch to non-root user
USER spring:spring

# Copy the built JAR
COPY --from=build /app/target/*.jar app.jar

# Expose port (Railway will override with PORT env var)
EXPOSE 8080

# Run the application with memory optimization for Railway
# Railway's health check will use the /api/health endpoint
ENTRYPOINT ["java", "-Xmx256m", "-Xms128m", "-jar", "app.jar"]
