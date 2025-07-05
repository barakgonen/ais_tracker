# Use a lightweight OpenJDK image
FROM eclipse-temurin:17-jdk

# Set the working directory

WORKDIR /app

# Copy JAR and config
COPY build/libs/*.jar app.jar

# Run the app with external config path
ENTRYPOINT ["java", "-jar", "app.jar"]
