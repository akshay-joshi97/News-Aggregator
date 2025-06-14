# Build stage (inside container - no local build needed)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY backend/pom.xml .
COPY backend/src ./src
RUN mvn clean package

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/backend/target/NewsAggregator2-1.0-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "NewsAggregator2-1.0-SNAPSHOT.jar"]