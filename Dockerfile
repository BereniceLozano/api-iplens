# Base image
FROM eclipse-temurin:17-jdk-jammy

# Workdir
WORKDIR /app

# Copiar archivos necesarios
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

# Dar permisos a mvnw dentro de la imagen
RUN chmod +x mvnw

# Construir la app
RUN ./mvnw clean package -DskipTests

# Exponer puerto
EXPOSE 9090

# Correr la app
CMD ["java", "-jar", "target/iplens-1.0.0.jar"]

