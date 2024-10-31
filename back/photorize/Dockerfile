# Stage 1: Build the application
FROM gradle:8.10.2-jdk17 AS build

# Set the working directory for the build stage
WORKDIR /app

# Copy the Gradle wrapper and build files
COPY ./gradlew /app/gradlew
COPY ./gradle /app/gradle
COPY ./build.gradle settings.gradle /app/

## 서버 프로필 활성화
#ENV SPRING_PROFILES_ACTIVE=server

# Run the Gradle build (this will produce the JAR file in build/libs)
RUN ./gradlew build

# Stage 2: Create the final image
FROM openjdk:17-jdk-slim

# Set the working directory for the final image
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 9090

## Run the application
#ENTRYPOINT ["java", "-jar", "app.jar"]

# Run the application with the server profile activated
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=server"]
