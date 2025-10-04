FROM eclipse-temurin:21-jdk

# Install pg_isready utility
RUN apt-get update && apt-get install -y postgresql-client && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY target/*.jar /app/app.jar
COPY wait-for-postgres.sh /app/wait-for-postgres.sh
RUN chmod +x /app/wait-for-postgres.sh

ENTRYPOINT ["sh", "/app/wait-for-postgres.sh", "role-db", "java", "-jar", "/app/app.jar"]