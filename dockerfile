FROM eclipse-temurin:21-jdk AS build

RUN apt-get update && apt-get install -y maven

WORKDIR /app

# Copier uniquement les fichiers Maven pour profiter du cache Docker
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .   # si tu utilises mvnw

RUN mvn dependency:go-offline

# Copier le code source ensuite
COPY src ./src

RUN mvn clean package -DskipTests
