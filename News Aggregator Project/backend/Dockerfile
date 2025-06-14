# Use a lightweight JDK 17 image
FROM eclipse-temurin:17-jre-jammy

# Set working directory
WORKDIR /app

# Copy ONLY the built JAR (no frontend needed)
COPY target/NewsAggregator2-1.0-SNAPSHOT.jar app.jar

# Expose your backend port (e.g., 8080)
EXPOSE 8080

# Run with memory limits (suitable for free tier)
CMD ["java", "-Xmx256m", "-jar", "app.jar"]