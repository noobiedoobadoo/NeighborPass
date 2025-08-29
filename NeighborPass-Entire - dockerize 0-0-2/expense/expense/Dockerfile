# Stage 2: Run the app (use slim JRE for smaller size)
FROM eclipse-temurin:21

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} authentication-mic.jar
# Or use ARG if building locally
EXPOSE 8082


# Run the app
ENTRYPOINT ["java", "-jar", "/authentication-mic.jar"]

