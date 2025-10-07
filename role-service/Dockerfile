FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
RUN apt-get update && apt-get install -y netcat-openbsd bash && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/target/*.jar app.jar

# Copy the wait-for-postgres script into the container
COPY wait-for-services.sh ./wait-for-services.sh

# ✅ Add this line for Windows users
RUN chmod +x /app/wait-for-services.sh


EXPOSE 8082
ENTRYPOINT ["sh", "-c", "./wait-for-services.sh postgres 5432 eureka-server 8761 -- java -jar app.jar"]

