FROM maven:3-sapmachine-26 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:26
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]