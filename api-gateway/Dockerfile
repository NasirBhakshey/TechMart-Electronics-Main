# Builder stage
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN apt-get update && apt-get install -y netcat-openbsd bash curl && rm -rf /var/lib/apt/lists/*

# Copy built jar
COPY --from=builder /app/target/*.jar app.jar

# Copy wait-for-services script
COPY wait-for-services.sh ./wait-for-services.sh
RUN chmod +x ./wait-for-services.sh

EXPOSE 8080
ENTRYPOINT ["./wait-for-services.sh", "--postgres", "postgres:5432", "--eureka", "eureka-server:8761", "--", "java", "-jar", "app.jar"]

