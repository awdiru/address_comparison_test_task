FROM eclipse-temurin:17-jdk-alpine
RUN apk add --no-cache curl netcat-openbsd
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]