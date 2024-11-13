# Stage 1: Build the application
FROM gradle:8.10.2-jdk17 AS build

# Set the working directory for the build stage
WORKDIR /app

# Copy all project files
COPY . /app

# Copy the Gradle wrapper and build files
COPY ./gradlew /app/gradlew
COPY ./gradle /app/gradle
COPY ./build.gradle settings.gradle /app/

# Run the Gradle build (this will produce the JAR file in build/libs)
RUN ./gradlew clean build -x test

# Stage 2: Create the final image
FROM openjdk:17-jdk-slim

# Set the working directory for the final image
WORKDIR /app

# Install tzdata and set timezone to Asia/Seoul
RUN apt-get update && apt-get install -y tzdata && \
    ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone && \
    apt-get clean

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/photorize-0.0.1-SNAPSHOT.jar app.jar

# Run the application with the server profile activated
ENTRYPOINT ["java", "-jar", "app.jar"]
