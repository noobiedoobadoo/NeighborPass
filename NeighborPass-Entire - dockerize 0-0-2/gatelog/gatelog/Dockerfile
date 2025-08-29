# Use a Java 21 base image to match your build version
FROM eclipse-temurin:21

# Copy the JAR (assumes it's built with Java 21)
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} gatelog.jar

# Expose the port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "/gatelog.jar"]
