FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-jammy
# Create a user for Hugging Face (UID 1000 is required)
RUN useradd -m -u 1000 user
USER user
ENV HOME=/home/user \
    PATH=/home/user/.local/bin:$PATH
WORKDIR $HOME/app

# Copy files from builder
COPY --from=builder --chown=user /app/target/*.jar app.jar

# Hugging Face uses port 7860 by default
EXPOSE 7860

ENTRYPOINT ["java", "-Xmx4g", "-Xms1g", "-jar", "app.jar", "--server.port=7860"]