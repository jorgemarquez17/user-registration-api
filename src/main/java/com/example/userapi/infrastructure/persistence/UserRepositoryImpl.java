package com.example.userapi.infrastructure.persistence;

import com.example.userapi.domain.model.User;
import com.example.userapi.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adapter Pattern - Adaptador que implementa la interfaz del dominio
 * delegando las operaciones al repositorio de Spring Data JPA.
 * Esto permite que el dominio no dependa de Spring Data JPA.
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    
    private final JpaUserRepository jpaUserRepository;
    
    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
    }
    
    @Override
    public Optional<User> findById(UUID id) {
        return jpaUserRepository.findById(id);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }
}
