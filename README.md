# Sistema de Gestión de Ninjas (Spring Boot + Docker)

Este es un proyecto Full-Stack que implementa una API REST con Spring Boot para gestionar ninjas, misiones y asignaciones. Incluye un frontend simple en HTML/JS para interactuar con el sistema.

El proyecto está diseñado para ser ejecutado de forma contenarizada usando Docker Compose.

---

## 📋 Requisitos Previos

Para ejecutar este proyecto, solo necesitas tener instalado:

* **Docker Desktop**: Asegúrate de que esté **iniciado y en ejecución** (el icono de la ballena debe estar estable) antes de continuar.

---

## 📂 Archivos de Configuración Faltantes

Para que Docker pueda construir y orquestar la aplicación, necesitas crear los siguientes 3 archivos en la **raíz de tu proyecto** (al mismo nivel que la carpeta `src` y `pom.xml` si ya la tienes).

### 1. `pom.xml` (Definición del Proyecto Java/Maven)

Este archivo le dice a Maven (el constructor de Java) cómo empaquetar tu aplicación y qué librerías (dependencias) necesita.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="[http://maven.apache.org/POM/4.0.0](http://maven.apache.org/POM/4.0.0)" xmlns:xsi="[http://www.w3.org/2001/XMLSchema-instance](http://www.w3.org/2001/XMLSchema-instance)"
    xsi:schemaLocation="[http://maven.apache.org/POM/4.0.0](http://maven.apache.org/POM/4.0.0) [https://maven.apache.org/xsd/maven-4.0.0.xsd](https://maven.apache.org/xsd/maven-4.0.0.xsd)">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.5</version> <relativePath/>
    </parent>
    
    <groupId>com.naruto</groupId>
    <artifactId>system</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>system</name>
    <description>Sistema de gestión de Ninjas</description>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
