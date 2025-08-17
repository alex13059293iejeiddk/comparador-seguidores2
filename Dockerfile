# Etapa 1: Build
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copiar el JAR generado desde la etapa builder
COPY --from=builder /app/target/comparador-seguidores-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto dinámico de Railway
ENV PORT 8080
EXPOSE $PORT
ENTRYPOINT ["sh","-c","java -Dserver.port=$PORT -jar app.jar"]

