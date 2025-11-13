package com.example.userapi.domain.service;

import com.example.userapi.domain.exception.BusinessException;
import com.example.userapi.domain.exception.ValidationException;
import com.example.userapi.domain.model.User;
import com.example.userapi.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

/**
 * Service Pattern - Servicio de dominio que contiene la lógica de negocio.
 * Esta clase no debe depender de detalles de infraestructura.
 */
@Service
@Slf4j

public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${validation.email.regexp}")
    private String emailRegexp;
    
    @Value("${validation.password.regexp}")
    private String passwordRegexp;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Strategy Pattern - Valida el formato del email segun expresion regular configurable
     */
    public void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("El correo es obligatorio");
        }
        
        Pattern pattern = Pattern.compile(emailRegexp);
        if (!pattern.matcher(email).matches()) {
            throw new ValidationException("El formato del correo es invalido");
        }
    }
    
    /**
     * Strategy Pattern - Valida el formato de la contraseña segun expresion regular configurable
     */
    public void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("La contraseña es obligatoria");
        }
        
        Pattern pattern = Pattern.compile(passwordRegexp);
        if (!pattern.matcher(password).matches()) {
            throw new ValidationException("El formato de la contraseña es invalido");
        }
    }
    
    /**
     * Valida que el email no este duplicado en el sistema
     */
    public void validateEmailNotDuplicated(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("El correo ya registrado");
        }
    }
    
    /**
     * Registra un nuevo usuario en el sistema
     * Aplica todas las validaciones de negocio
     */
    @Transactional
    public User registerUser(User user, String rawPassword) {
        log.info("Iniciando registro de usuario con email: {}", user.getEmail());
        
        // Validaciones de negocio
        validateEmail(user.getEmail());
        validatePassword(rawPassword);
        validateEmailNotDuplicated(user.getEmail());
        
        // Encriptar contraseña
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        
        // Activar usuario por defecto
        user.activate();
        
        // Persistir usuario
        User savedUser = userRepository.save(user);
        
        log.info("Usuario registrado exitosamente con ID: {}", savedUser.getId());
        return savedUser;
    }
    
    /**
     * Actualiza el token de un usuario
     */
    @Transactional
    public void updateUserToken(User user, String token) {
        user.updateToken(token);
        userRepository.save(user);
    }
}
