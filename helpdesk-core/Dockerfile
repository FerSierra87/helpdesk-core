# ---- Etapa 1: Compilar el proyecto con Maven ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos primero solo el pom.xml para aprovechar el cache de Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Ahora copiamos el resto del código y compilamos
COPY src ./src
RUN mvn clean package -DskipTests

# ---- Etapa 2: Imagen final, liviana, solo con el .jar ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
