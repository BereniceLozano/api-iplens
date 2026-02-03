# Use OpenJDK 17 como base
FROM eclipse-temurin:17-jdk-jammy

# Directorio de trabajo
WORKDIR /app

# Copia pom y fuentes
COPY pom.xml .
COPY src ./src

# Construir la app
RUN ./mvnw clean package -DskipTests

# Exponer el puerto
EXPOSE 9090

# Comando para correr
CMD ["java", "-jar", "target/iplens-1.0.0.jar"]
