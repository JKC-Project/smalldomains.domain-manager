# syntax=docker/dockerfile:1
FROM openjdk:16-alpine3.13 AS BUILDER
WORKDIR /app

# Copy over Maven Wrapper
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Copy src files
COPY src ./src

# Create JAR package
RUN ./mvnw package

# PREPARE RUNNABLE
FROM openjdk:16-alpine3.13 as RUN
WORKDIR /app
COPY --from=BUILDER /app/target/domain-manager.jar ./
EXPOSE 8080
CMD ["java", "-jar", "domain-manager.jar"]