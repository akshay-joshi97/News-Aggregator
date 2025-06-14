# Build stage (inside container - no local build needed)
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN cd backend && mvn clean package

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/backend/target/NewsAggregator2-1.0-SNAPSHOT.jar .
EXPOSE 8080
CMD ["java", "-jar", "NewsAggregator2-1.0-SNAPSHOT.jar"]