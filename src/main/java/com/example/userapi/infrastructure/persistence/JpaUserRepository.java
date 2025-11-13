package com.example.userapi.infrastructure.persistence;

import com.example.userapi.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementacion del repositorio usando Spring Data JPA.
 * Esta interfaz extiende JpaRepository para obtener las operaciones CRUD basicas.
 */
@Repository
public interface JpaUserRepository extends JpaRepository<User, UUID> {
    
    /**
     * Busca un usuario por su email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Verifica si existe un usuario con el email dado
     */
    boolean existsByEmail(String email);
}
