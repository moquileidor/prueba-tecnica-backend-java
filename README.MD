# Prueba Técnica Backend - Java Spring Boot

Proyecto desarrollado como parte del proceso de selección para **Broers S.A.S**, con el objetivo de demostrar buenas prácticas de desarrollo Backend, una arquitectura clara y un manejo adecuado de seguridad y autenticación.

##  Tecnologías utilizadas
- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security + JWT
- MySQL
- Swagger / OpenAPI
- HTML + CSS (Frontend básico para consumo de la API)

## Funcionalidades
- Registro de usuarios mediante correo electrónico
- Envío de correo con enlace y token para creación de contraseña
- Asignación de contraseña utilizando tokens de un solo uso
- Inicio de sesión con validación de credenciales y generación de JWT
- Recuperación de contraseña mediante correo electrónico
- Manejo de tokens con expiración y uso único
- Documentación de la API con Swagger

## Arquitectura del proyecto
El proyecto sigue una arquitectura por capas, separando responsabilidades para facilitar el mantenimiento y la escalabilidad:

- **Controller**: Exposición de endpoints REST
- **Service**: Lógica de negocio (interfaces e implementaciones)
- **Repository**: Acceso a datos con Spring Data JPA
- **Entity**: Modelos de base de datos
- **DTOs**: Objetos de transferencia para requests y responses
- **Security**: Configuración de autenticación y JWT

## Configuración y ejecución

### 1️ Base de datos
Crear una base de datos en MySQL:

```sql
CREATE DATABASE prueba_tecnica;
```

### 2️ Configuración
Configurar las credenciales de base de datos y correo en el archivo:

```
src/main/resources/application.yml
```

### 3 Ejecución
Desde la raíz del proyecto ejecutar:

```bash
./mvnw spring-boot:run
```

## Documentación API (Swagger)
Una vez el proyecto esté en ejecución, la documentación estará disponible en:

```
http://localhost:8080/swagger-ui/index.html
```

## Frontend básico
El proyecto incluye un frontend sencillo en HTML y CSS ubicado en `src/main/resources/static`, el cual permite consumir la API y probar los flujos principales.

Disponible en:

```
http://localhost:8080/
```

Incluye vistas para:
- Registro de usuario
- Creación de contraseña mediante token
- Inicio de sesión
- Recuperación de contraseña

## Notas finales
- El envío de correos puede configurarse usando un servicio SMTP real o herramientas de prueba como **Mailtrap**.
- El enfoque del proyecto está en la lógica backend, seguridad y buenas prácticas, más que en una interfaz gráfica compleja.

**Autor:** Jorge Luis Alvarado Cuesta
