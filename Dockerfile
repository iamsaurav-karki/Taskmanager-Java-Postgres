FROM eclipse-temurin:17-jre

# Create a non-root user
RUN addgroup --system appuser && \
    adduser --system --ingroup appuser appuser

WORKDIR /app

# Copy the JAR file
COPY target/task-manager-0.0.1-SNAPSHOT.jar app.jar

# Set proper ownership
RUN chown -R appuser:appuser /app

# Use the non-root user
USER appuser

# Expose the application port
EXPOSE 8080

# Configure JVM options
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]