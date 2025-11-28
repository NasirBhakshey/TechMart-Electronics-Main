# ============================
# 🔨 BUILD STAGE
# ============================
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Install Maven if mvnw is not available
RUN apt-get update && apt-get install -y maven

# Copy entire repository (common-dto + user-service)
COPY . .

# ---- Build common-dto first ----
WORKDIR /app/common-dto
RUN mvn clean install -DskipTests

# ---- Build user-service ----
WORKDIR /app/user-service
RUN mvn clean package -DskipTests


# ============================
# 🚀 RUNTIME STAGE
# ============================
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*
# Copy service JAR
COPY --from=builder /app/user-service/target/user-service-0.0.1-SNAPSHOT.jar app.jar

# Copy wait script
COPY --from=builder /app/user-service/wait-for-services.sh .
RUN chmod +x wait-for-services.sh

# Expose port
EXPOSE 8081

# ENTRYPOINT
ENTRYPOINT ["./wait-for-services.sh", \
            "--postgres", "postgres:5432", \
            "--eureka", "eureka-server:8761", \
            "--", "java", "-jar", "app.jar"]
