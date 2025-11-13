# API RESTful de Registro de Usuarios

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.5-blue.svg)](https://gradle.org/)


## 📋 Descripción

Aplicación Spring Boot que implementa una API RESTful para la creación y gestión de usuarios, utilizando **Domain-Driven Design (DDD)**, **JWT** para autenticación, y **H2** como base de datos en memoria.

## 🏗️ Arquitectura - Domain-Driven Design

El proyecto sigue los principios de **Domain-Driven Design (DDD)** con una arquitectura en capas.


## 🎯 Patrones de Diseño Implementados

| Patrón | Ubicación | Propósito |
|--------|-----------|-----------|
| **Repository** | `domain.repository` | Abstracción del acceso a datos |
| **Service** | `domain.service` | Encapsulación de lógica de negocio |
| **Use Case** | `application.usecase` | Orquestación de operaciones |
| **DTO** | `application.dto` | Transferencia de datos entre capas |
| **Mapper** | `application.mapper` | Transformación entre objetos |
| **Factory** | `infrastructure.security` | Creación de tokens JWT |
| **Strategy** | `domain.service` | Validaciones configurables |
| **Adapter** | `infrastructure.persistence` | Adaptación de repositorios |
| **Singleton** | Spring Beans | Gestión de instancias |

## 🚀 Tecnologías Utilizadas

- **Java 21** (OpenJDK Temurin)
- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **Spring Security**
- **H2 Database** (en memoria)
- **JWT** (JSON Web Token) - `io.jsonwebtoken:jjwt:0.12.3`
- **Swagger/OpenAPI 3.0**
- **Gradle 8.5**
- **JUnit 5** y **Mockito**
- **Lombok**

## 📋 Requisitos Previos

- **Java 21** (OpenJDK Temurin recomendado)
- **Gradle 8.5+** (incluido en el wrapper)

## ⚙️ Instalación y Ejecución

### 1️⃣ Clonar el repositorio

```bash
git clone <url-repositorio>
cd user-registration-api
```

### 2️⃣ Compilar el proyecto

```bash
./gradlew clean build
```

### 3️⃣ Ejecutar la aplicación

```bash
./gradlew bootRun
```

O usando el JAR generado:

```bash
java -jar build/libs/user-registration-api-1.0.0.jar
```

La aplicación estará disponible en: **http://localhost:8080**

## 📚 API Endpoints

### 📝 Registrar Usuario

**Endpoint:** `POST /api/users/register`

**Content-Type:** `application/json`

**Request Body:**
```json
{
  "name": "Jorge Marquez",
  "email": "jorge@marquez.org",
  "password": "Hunter22",
  "phones": [
    {
      "number": "1234567",
      "citycode": "1",
      "contrycode": "57"
    }
  ]
}
```

**Response exitosa (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Jorge Marquez",
  "email": "jorge@marquez.org",
  "phones": [
    {
      "number": "1234567",
      "citycode": "1",
      "contrycode": "57"
    }
  ],
  "created": "2025-11-12T10:30:00",
  "modified": "2025-11-12T10:30:00",
  "last_login": "2025-11-12T10:30:00",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "isactive": true
}
```

**Response con error (400 Bad Request):**
```json
{
  "mensaje": "El formato del correo es inválido"
}
```

**Response con error (409 Conflict):**
```json
{
  "mensaje": "El correo ya registrado"
}
```

## ✅ Validaciones

### Email
- **Formato:** `aaaaaaa@dominio.cl`
- **Expresión regular:** `^[a-z]+@[a-z]+\.[a-z]{2,}$`
- Solo letras minúsculas permitidas
- Configurable en `application.properties`

### Password
- **Formato por defecto:** Al menos una mayúscula, letras minúsculas y dos números
- **Expresión regular:** `^(?=.*[A-Z])(?=.*[a-z])(?=.*\d.*\d)[A-Za-z\d]{8,}$`
- Mínimo 8 caracteres
- Configurable en `application.properties`

## ⚙️ Configuración

El archivo `src/main/resources/application.properties` permite configurar:

```properties
# Validación de Email
validation.email.regexp=^[a-z]+@[a-z]+\\.[a-z]{2,}$

# Validación de Password
validation.password.regexp=^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d.*\\d)[A-Za-z\\d]{8,}$

# JWT
jwt.secret=miClaveSecretaSuperSeguraParaJWT2025DebeSerLargaYCompleja
jwt.expiration=86400000

# H2 Database
spring.datasource.url=jdbc:h2:mem:userdb
spring.h2.console.enabled=true
```

## 📖 Documentación Swagger

Acceder a la documentación interactiva de la API:

```
http://localhost:8080/swagger-ui.html
```

API Docs JSON:
```
http://localhost:8080/api-docs
```

## 💾 Base de Datos H2 Console

Acceder a la consola H2:

```
http://localhost:8080/h2-console
```

**Credenciales:**
- **JDBC URL:** `jdbc:h2:mem:userdb`
- **Username:** `sa`
- **Password:** _(dejar vacío)_

## 🧪 Pruebas

### Ejecutar todas las pruebas

```bash
./gradlew test
```

### Ejecutar con reporte de cobertura

```bash
./gradlew test jacocoTestReport
```

El reporte de cobertura estará en: `build/reports/jacoco/test/html/index.html`

### Pruebas Implementadas

- ✅ **UserServiceTest:** Tests de lógica de negocio
- ✅ **RegisterUserUseCaseTest:** Tests de caso de uso
- ✅ **UserControllerTest:** Tests de integración del controlador
- ✅ **JwtTokenProviderTest:** Tests de generación y validación JWT

## 📊 Diagrama de Solución

```
┌─────────────────────────────────────────────────────────────────┐
│                     CLIENTE (Postman/Browser)                    │
└────────────────────────────┬────────────────────────────────────┘
                             │ HTTP/JSON
                             ▼
┌────────────────────────────────────────────────────────────────┐
│               PRESENTATION LAYER (REST API)                     │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ UserController                                           │  │
│  │  └─ POST /api/users/register                            │  │
│  └──────────────────┬───────────────────────────────────────┘  │
│                     │                                           │
│  ┌──────────────────▼───────────────────────────────────────┐  │
│  │ GlobalExceptionHandler                                   │  │
│  │  ├─ ValidationException → 400                           │  │
│  │  ├─ BusinessException → 409                             │  │
│  │  └─ Exception → 500                                     │  │
│  └──────────────────────────────────────────────────────────┘  │
└────────────────────────────┬───────────────────────────────────┘
                             │
                             ▼
┌────────────────────────────────────────────────────────────────┐
│                   APPLICATION LAYER                             │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ RegisterUserUseCase                                      │  │
│  │  1. Map DTO → Domain Entity                             │  │
│  │  2. Execute Business Logic                              │  │
│  │  3. Generate JWT Token                                  │  │
│  │  4. Map Domain Entity → DTO                             │  │
│  └──────────────────┬───────────────────────────────────────┘  │
│                     │                                           │
│  ┌──────────────────▼───────────────────────────────────────┐  │
│  │ DTOs & Mappers                                           │  │
│  │  ├─ RegisterUserRequest                                 │  │
│  │  ├─ UserResponse                                        │  │
│  │  └─ UserMapper                                          │  │
│  └──────────────────────────────────────────────────────────┘  │
└────────────────────────────┬───────────────────────────────────┘
                             │
                             ▼
┌────────────────────────────────────────────────────────────────┐
│                      DOMAIN LAYER                               │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ UserService (Business Logic)                             │  │
│  │  ├─ validateEmail()                                      │  │
│  │  ├─ validatePassword()                                   │  │
│  │  ├─ validateEmailNotDuplicated()                        │  │
│  │  └─ registerUser()                                       │  │
│  └──────────────────┬───────────────────────────────────────┘  │
│                     │                                           │
│  ┌──────────────────▼───────────────┬───────────────────────┐  │
│  │ User (Aggregate Root)            │ Phone (Value Object)  │  │
│  │  ├─ id: UUID                     │  ├─ number            │  │
│  │  ├─ name                          │  ├─ citycode         │  │
│  │  ├─ email                         │  └─ contrycode       │  │
│  │  ├─ password                      └───────────────────────┘  │
│  │  ├─ phones: List<Phone>                                  │  │
│  │  ├─ token                                                 │  │
│  │  ├─ created                                               │  │
│  │  ├─ modified                                              │  │
│  │  ├─ lastLogin                                             │  │
│  │  └─ isactive                                              │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ UserRepository (Interface)                               │  │
│  │  ├─ save()                                               │  │
│  │  ├─ findByEmail()                                        │  │
│  │  └─ existsByEmail()                                      │  │
│  └──────────────────┬───────────────────────────────────────┘  │
└───────────────────────────────┬───────────────────────────────┘
                                │
                                ▼
┌────────────────────────────────────────────────────────────────┐
│                 INFRASTRUCTURE LAYER                            │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ UserRepositoryImpl (Adapter)                             │  │
│  │  └─ delegates to ──▶ JpaUserRepository                   │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ JwtTokenProvider (Factory)                               │  │
│  │  ├─ generateToken()                                      │  │
│  │  └─ validateToken()                                      │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ SecurityConfig                                           │  │
│  │  └─ BCryptPasswordEncoder                                │  │
│  └──────────────────────────────────────────────────────────┘  │
└────────────────────────────┬───────────────────────────────────┘
                             │
                             ▼
┌────────────────────────────────────────────────────────────────┐
│                    H2 DATABASE (In-Memory)                      │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ USERS                                                    │  │
│  │  ├─ id (UUID, PK)                                        │  │
│  │  ├─ name (VARCHAR)                                       │  │
│  │  ├─ email (VARCHAR, UNIQUE)                              │  │
│  │  ├─ password (VARCHAR)                                   │  │
│  │  ├─ token (TEXT)                                         │  │
│  │  ├─ created (TIMESTAMP)                                  │  │
│  │  ├─ modified (TIMESTAMP)                                 │  │
│  │  ├─ last_login (TIMESTAMP)                               │  │
│  │  └─ isactive (BOOLEAN)                                   │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ PHONES                                                   │  │
│  │  ├─ id (BIGINT, PK, AUTO)                                │  │
│  │  ├─ number (VARCHAR)                                     │  │
│  │  ├─ citycode (VARCHAR)                                   │  │
│  │  ├─ contrycode (VARCHAR)                                 │  │
│  │  └─ user_id (UUID, FK → users.id)                        │  │
│  └──────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────┘
```

## 🔍 Ejemplos de Uso

### Con cURL

#### ✅ Registrar un nuevo usuario

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jorge Marquez",
    "email": "jorge@marquez.org",
    "password": "Hunter22",
    "phones": [
      {
        "number": "1234567",
        "citycode": "1",
        "contrycode": "57"
      }
    ]
  }'
```

#### ❌ Intentar registrar email duplicado

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Pedro Perez",
    "email": "jorge@marquez.org",
    "password": "Hunter22",
    "phones": []
  }'
```

**Respuesta esperada:**
```json
{
  "mensaje": "El correo ya registrado"
}
```

### Con Postman

1. **Importar la colección** desde Swagger
2. **Configurar el endpoint:** `POST http://localhost:8080/api/users/register`
3. **Headers:** `Content-Type: application/json`
4. **Body (raw JSON):** Copiar el JSON de ejemplo

## 📊 Estructura de la Base de Datos

### Tabla `users`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | UUID | Identificador único (PK) |
| name | VARCHAR(255) | Nombre completo |
| email | VARCHAR(255) | Correo electrónico (UNIQUE) |
| password | VARCHAR(255) | Contraseña encriptada (BCrypt) |
| token | TEXT | Token JWT |
| created | TIMESTAMP | Fecha de creación |
| modified | TIMESTAMP | Fecha de última modificación |
| last_login | TIMESTAMP | Fecha del último login |
| isactive | BOOLEAN | Estado del usuario |

### Tabla `phones`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT | Identificador único (PK, AUTO) |
| number | VARCHAR(50) | Número de teléfono |
| citycode | VARCHAR(10) | Código de ciudad |
| contrycode | VARCHAR(10) | Código de país |
| user_id | UUID | Referencia al usuario (FK) |

## 🎓 Principios SOLID Aplicados

- **S**ingle Responsibility: Cada clase tiene una única responsabilidad
- **O**pen/Closed: Abierto para extensión, cerrado para modificación
- **L**iskov Substitution: Interfaces implementadas correctamente
- **I**nterface Segregation: Interfaces específicas y cohesivas
- **D**ependency Inversion: Dependencias a través de abstracciones

## 🛡️ Seguridad

- ✅ Contraseñas encriptadas con **BCrypt**
- ✅ Tokens **JWT** con firma HMAC-SHA256
- ✅ Validación de entrada con **Bean Validation**
- ✅ Expresiones regulares configurables
- ✅ Manejo seguro de excepciones

## 📦 Características Implementadas

- ✅ API RESTful con Spring Boot
- ✅ Base de datos H2 en memoria
- ✅ Arquitectura Domain-Driven Design (DDD)
- ✅ JWT para autenticación
- ✅ Validaciones con expresiones regulares configurables
- ✅ Manejo de errores global con formato JSON
- ✅ Swagger/OpenAPI para documentación
- ✅ Pruebas unitarias con JUnit 5 y Mockito
- ✅ UUIDs para identificadores
- ✅ Encriptación de contraseñas con BCrypt
- ✅ Patrones de diseño (10+ patrones implementados)
- ✅ Código limpio y principios SOLID
- ✅ Respuestas HTTP apropiadas (201, 400, 409, 500)
- ✅ Gradle como gestor de dependencias
- ✅ Java 21 (OpenJDK Temurin)

## 👤 Autor

**Jorge** - Senior Full Stack Java Developer
