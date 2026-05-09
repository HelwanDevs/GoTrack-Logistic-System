FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:21-alpine
WORKDIR /app
RUN mkdir -p /app/keys
COPY --from=build /app/target/*.jar app.jar
COPY keys/gateway_private_key.pem /app/keys/gateway_private_key.pem
COPY keys/gateway_public_key.pem /app/keys/gateway_public_key.pem
COPY keys/client_public_key.pem /app/keys/client_public_key.pem
ENTRYPOINT ["java", "-jar", "app.jar"]
