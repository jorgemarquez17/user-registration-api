-- ========================================
-- Script de Creacion de Base de Datos
-- ========================================

-- Tabla de Usuarios
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    token TEXT,
    created TIMESTAMP NOT NULL,
    modified TIMESTAMP NOT NULL,
    last_login TIMESTAMP NOT NULL,
    isactive BOOLEAN NOT NULL DEFAULT TRUE
);

-- Indice para busquedas por email
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- Tabla de Telefonos
CREATE TABLE IF NOT EXISTS phones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(50) NOT NULL,
    citycode VARCHAR(10),
    contrycode VARCHAR(10),
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Indice para busquedas por user_id
CREATE INDEX IF NOT EXISTS idx_phones_user_id ON phones(user_id);

-- Nota: La aplicacion crea automaticamente las tablas
-- gracias, mediante Hibernate (ddl-auto=create-drop)
-- Este script es principalmente para referencia y documentacion.
