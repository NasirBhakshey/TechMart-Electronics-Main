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

# Copy wait-for-services script (if needed)
COPY wait-for-postgres.sh ./wait-for-postgres.sh
RUN chmod +x ./wait-for-postgres.sh

EXPOSE 8761
ENTRYPOINT ["bash", "-c", "./wait-for-postgres.sh postgres 5432 java -jar app.jar"]
