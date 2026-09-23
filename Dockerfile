# ==========================================
# STAGE 1: Build the application
# ==========================================
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the executable JAR file
COPY src ./src
RUN mvn package -DskipTests

# ==========================================
# STAGE 2: Run the application
# ==========================================
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the generated JAR file from Stage 1
COPY --from=build /app/target/*.jar app.jar

# Render exposes the port via the PORT environment variable
EXPOSE 8080

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]