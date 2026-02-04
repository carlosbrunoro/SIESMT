# Estágio 1: Build
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copia o pom de dentro da pasta backend
COPY backend/pom.xml .
RUN mvn dependency:go-offline

# Copia a pasta src de dentro da pasta backend
COPY backend/src ./src
RUN mvn clean package

# Estágio 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]