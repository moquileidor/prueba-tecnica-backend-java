# Prueba Técnica - Backend Spring Boot

Sistema de gestión de usuarios con autenticación JWT, registro, configuración de contraseña y recuperación de contraseña.

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 4.0.1**
- **Spring Security** con JWT
- **Spring Data JPA** (ORM)
- **MySQL** como base de datos
- **JavaMailSender** para envío de correos
- **Swagger/OpenAPI** para documentación
- **Lombok** para reducir código boilerplate
- **BCrypt** para encriptación de contraseñas

## Características Implementadas

### Funcionalidades Completadas

1. **Registro de Usuarios**
   - Campos: Nombre completo y correo electrónico
   - Envío automático de correo con token personalizado
   - Token válido por 24 horas

2. **Configuración de Contraseña**
   - Asignación de contraseña usando token
   - Validación de token único (no reutilizable)
   - Activación automática del usuario

3. **Inicio de Sesión**
   - Validación de credenciales (email y contraseña)
   - Verificación de usuario activo
   - Generación de token JWT con expiración de 24 horas

4. **Recuperación de Contraseña**
   - Solicitud de recuperación por correo
   - Envío de enlace personalizado con token
   - Token válido por 1 hora
   - Restablecimiento de contraseña con token único

5. **Gestión de Usuarios**
   - Endpoint para listar todos los usuarios
   - Endpoint para obtener usuario por ID
   - Protegidos con JWT

6. **Documentación API**
   - Swagger UI disponible en `/swagger-ui.html`
   - Documentación completa de todos los endpoints

## Arquitectura

```
src/main/java/org/jorge/pruebatecnica/
├── config/              # Configuración de Swagger
├── controller/          # Controladores REST
│   ├── AuthController   # Gestión de autenticación
│   └── UserController   # Gestión de usuarios
├── dto/
│   ├── request/         # DTOs de entrada
│   └── response/        # DTOs de salida
├── entity/              # Entidades JPA
│   ├── User
│   └── Token
├── enums/
│   └── TokenType        # REGISTRATION, PASSWORD_RESET
├── exception/           # Manejo de excepciones
├── repository/          # Repositorios JPA
├── security/            # Configuración de seguridad y JWT
└── service/             # Lógica de negocio
    └── impl/
```

## Configuración

### 1. Base de Datos MySQL

Crea una base de datos MySQL o configura las credenciales en `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/prueba_tecnica_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
```

### 2. Configuración de Correo Electrónico

Configura tu servidor SMTP en `application.properties`:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=tu-email@gmail.com
spring.mail.password=tu-app-password
```

**Para Gmail:**
1. Habilita la verificación en dos pasos
2. Genera una contraseña de aplicación en: https://myaccount.google.com/apppasswords
3. Usa esa contraseña en `spring.mail.password`

### 3. Configuración JWT

El secreto JWT está preconfigurado, pero puedes cambiarlo en `application.properties`:

```properties
jwt.secret=tu-secreto-base64
jwt.expiration=86400000  # 24 horas en milisegundos
```

##  Ejecución

### Con Maven

```bash
# Compilar el proyecto
./mvnw clean install

# Ejecutar la aplicación
./mvnw spring-boot:run
```

### Con IDE

Ejecuta la clase principal `PruebaTecnicaApplication.java`

La aplicación estará disponible en: `http://localhost:8080`

##  Documentación API (Swagger)

Una vez iniciada la aplicación, accede a:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs

##  Endpoints Principales

### Autenticación (No requieren autenticación)

#### Registro de Usuario
```http
POST /api/auth/register
Content-Type: application/json

{
  "fullName": "Juan Pérez",
  "email": "juan.perez@example.com"
}
```

#### Configurar Contraseña
```http
POST /api/auth/set-password
Content-Type: application/json

{
  "token": "abc123xyz...",
  "password": "miPassword123"
}
```

#### Inicio de Sesión
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "juan.perez@example.com",
  "password": "miPassword123"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "juan.perez@example.com",
  "fullName": "Juan Pérez"
}
```

#### Solicitar Recuperación de Contraseña
```http
POST /api/auth/forgot-password
Content-Type: application/json

{
  "email": "juan.perez@example.com"
}
```

#### Restablecer Contraseña
```http
POST /api/auth/reset-password
Content-Type: application/json

{
  "token": "xyz789abc...",
  "newPassword": "nuevaPassword456"
}
```

### Usuarios (Requieren JWT)

Para usar estos endpoints, incluye el token JWT en el header:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Obtener Todos los Usuarios
```http
GET /api/users
Authorization: Bearer {token}
```

#### Obtener Usuario por ID
```http
GET /api/users/{id}
Authorization: Bearer {token}
```

##  Modelo de Datos

### Usuario (users)
- `id`: Long (PK, Auto-generado)
- `full_name`: String
- `email`: String (único)
- `password`: String (encriptado)
- `active`: Boolean
- `created_at`: LocalDateTime
- `updated_at`: LocalDateTime

### Token (tokens)
- `id`: Long (PK, Auto-generado)
- `token`: String (único)
- `type`: Enum (REGISTRATION, PASSWORD_RESET)
- `used`: Boolean
- `expires_at`: LocalDateTime
- `created_at`: LocalDateTime
- `user_id`: Long (FK)

##  Seguridad

- Las contraseñas se almacenan encriptadas con **BCrypt**
- Los tokens de registro/recuperación son **UUID aleatorios**
- Los tokens tienen **fecha de expiración**
- Los tokens solo pueden **usarse una vez**
- JWT con expiración de **24 horas**
- Validación de usuario activo en login
- CORS configurado para permitir acceso desde frontend

##  Flujo de Uso

### 1. Registro de Usuario
1. Usuario envía datos de registro (nombre, email)
2. Sistema crea usuario inactivo
3. Sistema genera token de registro
4. Sistema envía correo con enlace personalizado
5. Usuario hace clic en el enlace
6. Usuario configura su contraseña
7. Sistema activa la cuenta

### 2. Inicio de Sesión
1. Usuario envía credenciales (email, password)
2. Sistema valida credenciales
3. Sistema verifica que el usuario esté activo
4. Sistema genera y retorna token JWT
5. Usuario usa el token para acceder a endpoints protegidos

### 3. Recuperación de Contraseña
1. Usuario solicita recuperación (email)
2. Sistema genera token de recuperación
3. Sistema envía correo con enlace
4. Usuario hace clic en el enlace
5. Usuario establece nueva contraseña
6. Token se marca como usado

##  Pruebas

Puedes probar la API usando:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Postman**: Importa la colección desde Swagger
- **cURL**: Ejemplos en la documentación

##  Interfaces Implementadas

### Repositorios (Interfaces JPA)
- `UserRepository extends JpaRepository<User, Long>`
- `TokenRepository extends JpaRepository<Token, Long>`

### Servicios (Interfaces)
- `UserService` con métodos de negocio
- `UserDetailsService` de Spring Security

##  Frontend (Incluido)

El proyecto incluye páginas HTML en `src/main/resources/static/`:
- `index.html` - Página principal
- `login.html` - Inicio de sesión
- `set-password.html` - Configuración de contraseña
- `reset.html` - Restablecer contraseña
- `forgot.html` - Solicitar recuperación

##  Manejo de Errores

El sistema incluye manejo global de excepciones con respuestas estructuradas:

```json
{
  "timestamp": "2026-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El correo electrónico ya está registrado",
  "path": "/api/auth/register"
}
```

##  Dependencias Principales

```xml
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Mail
- Spring Boot Starter Validation
- MySQL Connector
- JJWT (JWT)
- Lombok
- SpringDoc OpenAPI (Swagger)
```

##  Autor
Jorge - Prueba Técnica Backend

