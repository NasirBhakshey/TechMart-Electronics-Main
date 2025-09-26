FROM openjdk:21
WORKDIR /app
COPY target/role-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8061
ENTRYPOINT ["java", "-jar", "app.jar"]