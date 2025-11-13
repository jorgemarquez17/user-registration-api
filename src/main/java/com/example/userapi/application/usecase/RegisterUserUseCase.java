package com.example.userapi.application.usecase;

import com.example.userapi.application.dto.RegisterUserRequest;
import com.example.userapi.application.dto.UserResponse;
import com.example.userapi.application.mapper.UserMapper;
import com.example.userapi.domain.model.User;
import com.example.userapi.domain.service.UserService;
import com.example.userapi.infrastructure.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case Pattern - Caso de uso para el registro de usuarios.
 * Orquesta las operaciones necesarias para completar el caso de uso.
 * Coordina entre la capa de dominio y la infraestructura.
 */
@Component
@Slf4j
public class RegisterUserUseCase {


    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public RegisterUserUseCase(UserService userService, UserMapper userMapper, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    /**
     * Ejecuta el caso de uso de registro de usuario
     * 
     * @param request Datos del usuario a registrar
     * @return UserResponse con los datos del usuario registrado y su token
     */
    @Transactional
    public UserResponse execute(RegisterUserRequest request) {
        log.info("Ejecutando caso de uso: Registrar usuario");
        
        // 1. Convertir DTO a entidad de dominio
        User user = userMapper.toEntity(request);
        
        // 2. Registrar usuario (se ejecuta la logica de negocio)
        User registeredUser = userService.registerUser(user, request.getPassword());
        
        // 3. Generar token JWT
        String token = jwtTokenProvider.generateToken(registeredUser.getEmail());
        
        // 4. Actualizar el token en el usuario
        userService.updateUserToken(registeredUser, token);
        
        // 5. Convertir entidad de dominio a DTO de respuesta
        UserResponse response = userMapper.toResponse(registeredUser);
        
        log.info("Usuario registrado exitosamente: {}", registeredUser.getEmail());
        return response;
    }
}
