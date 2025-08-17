# Usa tu imagen base Java
FROM eclipse-temurin:23-jdk-jammy

# Copia el jar al contenedor
COPY target/comparador-seguidores-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto dinámico que Railway asigna
ENV PORT 8080
EXPOSE $PORT

# Arranca la app usando la variable PORT
ENTRYPOINT ["java","-Dserver.port=$PORT","-jar","/app.jar"]
