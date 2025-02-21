# Use Maven to build the application
FROM maven:3.8.6-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
COPY --from=build /target/lighthouse-0.0.1-SNAPSHOT.jar lighthouse.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","lighthouse.jar"]