FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/target/*.jar app.jar

# Copy the wait-for-postgres script into the container
COPY wait-for-postgres.sh ./wait-for-postgres.sh

# ✅ Add this line for Windows users
RUN chmod +x ./wait-for-postgres.sh

EXPOSE 8761
ENTRYPOINT ["sh", "-c", "./wait-for-postgres.sh postgres 5432 java -jar app.jar"]

