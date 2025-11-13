# Diagrama de Arquitectura

## Flujo de Registro de Usuario

```mermaid
sequenceDiagram
    participant Client as Cliente
    participant Controller as UserController
    participant UseCase as RegisterUserUseCase
    participant Mapper as UserMapper
    participant Service as UserService
    participant Repo as UserRepository
    participant JWT as JwtTokenProvider
    participant DB as H2 Database

    Client->>Controller: POST /api/users/register
    Controller->>UseCase: execute(RegisterUserRequest)
    UseCase->>Mapper: toEntity(request)
    Mapper-->>UseCase: User entity
    UseCase->>Service: registerUser(user, password)
    
    Service->>Service: validateEmail(email)
    Service->>Service: validatePassword(password)
    Service->>Repo: existsByEmail(email)
    Repo->>DB: SELECT * FROM users WHERE email=?
    DB-->>Repo: false
    Repo-->>Service: false
    
    Service->>Service: encryptPassword(password)
    Service->>Repo: save(user)
    Repo->>DB: INSERT INTO users...
    DB-->>Repo: User saved
    Repo-->>Service: User with ID
    Service-->>UseCase: Registered User
    
    UseCase->>JWT: generateToken(email)
    JWT-->>UseCase: JWT Token
    UseCase->>Service: updateUserToken(user, token)
    Service->>Repo: save(user)
    Repo->>DB: UPDATE users SET token=?
    DB-->>Repo: Updated
    Repo-->>Service: Updated User
    Service-->>UseCase: User with Token
    
    UseCase->>Mapper: toResponse(user)
    Mapper-->>UseCase: UserResponse
    UseCase-->>Controller: UserResponse
    Controller-->>Client: 201 Created + UserResponse JSON
```

## Capas DDD

```mermaid
graph TB
    subgraph Presentation["🖥️ PRESENTATION LAYER"]
        Controller[UserController]
        ExHandler[GlobalExceptionHandler]
    end

    subgraph Application["📋 APPLICATION LAYER"]
        UseCase[RegisterUserUseCase]
        DTOs[DTOs & Mappers]
    end

    subgraph Domain["💼 DOMAIN LAYER"]
        Service[UserService]
        Entity[User Entity]
        ValueObj[Phone Value Object]
        RepoInt[UserRepository Interface]
    end

    subgraph Infrastructure["⚙️ INFRASTRUCTURE LAYER"]
        RepoImpl[UserRepositoryImpl]
        JpaRepo[JpaUserRepository]
        JWT[JwtTokenProvider]
        Security[SecurityConfig]
    end

    subgraph Database["💾 DATABASE"]
        H2[(H2 In-Memory)]
    end

    Controller --> UseCase
    Controller --> ExHandler
    UseCase --> DTOs
    UseCase --> Service
    UseCase --> JWT
    Service --> RepoInt
    Service --> Entity
    Entity --> ValueObj
    RepoInt -.implements.- RepoImpl
    RepoImpl --> JpaRepo
    JpaRepo --> H2

    style Presentation fill:#e1f5ff
    style Application fill:#fff4e1
    style Domain fill:#e8f5e9
    style Infrastructure fill:#f3e5f5
    style Database fill:#fce4ec
```

## Patrones de Diseño

```mermaid
mindmap
  root((Patrones de Diseño))
    Creacionales
      Factory Pattern
        JwtTokenProvider
      Builder Pattern
        User.builder()
        DTOs builders
      Singleton Pattern
        Spring Beans
    Estructurales
      Adapter Pattern
        UserRepositoryImpl
      DTO Pattern
        RegisterUserRequest
        UserResponse
      Mapper Pattern
        UserMapper
    Comportamiento
      Strategy Pattern
        Email Validation
        Password Validation
        PasswordEncoder
      Repository Pattern
        UserRepository
      Service Pattern
        UserService
      Use Case Pattern
        RegisterUserUseCase
```

## Modelo de Dominio

```mermaid
classDiagram
    class User {
        -UUID id
        -String name
        -String email
        -String password
        -List~Phone~ phones
        -String token
        -LocalDateTime created
        -LocalDateTime modified
        -LocalDateTime lastLogin
        -Boolean isactive
        +activate()
        +deactivate()
        +updateLastLogin()
        +updateToken(String)
    }

    class Phone {
        -String number
        -String citycode
        -String contrycode
    }

    class UserRepository {
        <<interface>>
        +save(User) User
        +findByEmail(String) Optional~User~
        +existsByEmail(String) boolean
    }

    class UserService {
        -UserRepository repository
        -PasswordEncoder encoder
        +registerUser(User, String) User
        +validateEmail(String)
        +validatePassword(String)
        +updateUserToken(User, String)
    }

    User "1" *-- "*" Phone : contains
    UserService --> UserRepository : uses
    UserService --> User : manages
```
