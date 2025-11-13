package com.example.userapi.domain.repository;

import com.example.userapi.domain.model.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository Pattern - Interface del repositorio de usuarios.
 * Define el contrato para el acceso a datos en la capa de dominio.
 * La implementacion esta en la capa de infraestructura.
 */
public interface UserRepository {
    
    /**
     * Guarda un usuario en la base de datos
     * @param user Usuario a guardar
     * @return Usuario guardado con su ID generado
     */
    User save(User user);
    
    /**
     * Busca un usuario por su email
     * @param email Email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Busca un usuario por su ID
     * @param id ID del usuario
     * @return Optional con el usuario si existe
     */
    Optional<User> findById(UUID id);
    
    /**
     * Verifica si existe un usuario con el email dado
     * @param email Email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);
}
