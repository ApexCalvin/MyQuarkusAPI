# Build Stage: Use Maven as the base image for building in container 1
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Create and cd into /app
WORKDIR /app

# Copy only the pom
COPY pom.xml .

# Download project dependencies from pom without actually compiling the code
RUN mvn dependency:go-offline

# Copy over src code
COPY src ./src

# Compile and package the app into a .jar file. Skip tests to save time. 
RUN mvn clean package -DskipTests


# Run Stage: Use Temurin JDK as the base image for running in container 2
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy the entire quarkus-app directory (FAST-JAR output)
COPY --from=build /app/target/quarkus-app/ /app/

# Install netcat in the runtime image so the included wait script can use `nc` to probe MySQL
RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*

# Start the Quarkus application. Although Docker Compose starts MySQL first, we need to wait until it's fully ready.
COPY wait-for-mysql.sh /app/wait-for-mysql.sh
RUN chmod +x /app/wait-for-mysql.sh
CMD ["/app/wait-for-mysql.sh"]
