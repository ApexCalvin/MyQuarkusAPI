#Stage 1, (Maven) Build stage in Container 1: Update files/code if needed and build the .jar file
FROM maven:3.9.6-eclipse-temurin-21 AS build
RUN echo "Build base image for container 1"

# Set the working directory inside the container
WORKDIR /app
RUN echo "Create and move into app dir for container 1"

# Copy only the pom.xml to leverage Docker cache for dependency resolution
COPY pom.xml .
RUN echo "Copied pom to container 1"

# Download project dependencies (without compiling the full source yet)
RUN mvn dependency:go-offline
RUN echo "Downloaded dependencies only and cached"

# Copy the source code after dependencies are cached
COPY src ./src
RUN echo "Copied src code to container 1"

# Compile and package the application into a .jar file (skipping tests to speed up build)
RUN mvn clean package -DskipTests
RUN echo "Compiled source code and packaged application into a .jar file an existing or generated target dir"
RUN echo "Build stage completed."

#RUN ls -lh target
RUN echo "List all files in folder to validate .jar file in container 1"
RUN echo "Files in /app/target:" && ls -lh target
#[build 15/15]

#Stage 2, Run stage in Container 2: Run the .jar file from Container 1 into this container
FROM eclipse-temurin:21-jdk
RUN echo "Build base image for container 2"

# Set working directory
WORKDIR /app
RUN echo "Create and move into app dir for container 2"

# Copy your application JAR to this container
COPY --from=build /app/target/quarkus-demo-1.0.0-SNAPSHOT.jar app.jar
#COPY ./target/quarkus-app/quarkus-run.jar app.jar
RUN echo "Copied .jar file from container 1 to container 2"

# Run the application
RUN echo "Building docker image from .jar file"
#[stage-1 7/7]
CMD ["java", "-jar", "app.jar"]