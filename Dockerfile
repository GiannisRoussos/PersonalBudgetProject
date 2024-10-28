# First stage: Build the JAR file
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app  # Set working directory
COPY . /app   # Copy project files to /app
RUN mvn clean package -DskipTests

# Second stage: Create a lightweight container for the JAR
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /app/target/PersonalBudget-0.0.1-SNAPSHOT.jar PersonalBudget.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","PersonalBudget.jar"]