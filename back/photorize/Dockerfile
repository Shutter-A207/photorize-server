# Stage 1: Build the application
FROM gradle:8.10.2-jdk17 AS build
WORKDIR /app

# Optimize caching by copying build files first
COPY gradlew /app/gradlew
COPY gradle /app/gradle
COPY build.gradle settings.gradle /app/
RUN ./gradlew build -x test --no-daemon || return 0

# Now copy the rest of the source code
COPY . /app
RUN ./gradlew clean build -x test

# Stage 2: Create the final image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Install tzdata and clean up
RUN apt-get update && apt-get install -y tzdata && \
    ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/photorize-0.0.1-SNAPSHOT.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
