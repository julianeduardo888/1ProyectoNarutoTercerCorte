# ----- Etapa 1: Construcción (Build Stage) -----
# Usamos una imagen de Maven con Java 17 para compilar el proyecto
FROM maven:3.8-openjdk-17 AS builder

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos primero el pom.xml para aprovechar el caché de capas de Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el resto del código fuente
COPY src ./src

# Compilamos el proyecto y creamos el .jar. Saltamos los tests.
RUN mvn clean package -DskipTests


# ----- Etapa 2: Ejecución (Runtime Stage) -----
# Usamos una imagen ligera de Java 17 (solo el JRE) para ejecutar la app
FROM openjdk:17-alpine

WORKDIR /app

# Copiamos el .jar construido desde la etapa 'builder'
# (Asegúrate de que el nombre del artifact en tu pom.xml sea 'system')
COPY --from=builder /app/target/system-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8080 (el que usa Spring Boot)
EXPOSE 8080

# El comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]