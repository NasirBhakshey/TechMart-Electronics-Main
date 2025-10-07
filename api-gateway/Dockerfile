FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
RUN apt-get update && apt-get install -y netcat-openbsd bash && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/target/*.jar app.jar
COPY wait-for-services.sh ./wait-for-services.sh

RUN chmod +x ./wait-for-services.sh

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "./wait-for-services.sh --eureka eureka-server:8761 -- java -jar app.jar"]
