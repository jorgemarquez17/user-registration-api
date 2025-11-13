package com.example.userapi.application.usecase;

import com.example.userapi.application.dto.RegisterUserRequest;
import com.example.userapi.application.dto.UserResponse;
import com.example.userapi.application.mapper.UserMapper;
import com.example.userapi.domain.model.User;
import com.example.userapi.domain.service.UserService;
import com.example.userapi.infrastructure.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterUserUseCase - Tests de Caso de Uso")
class RegisterUserUseCaseTest {
    
    @Mock
    private UserService userService;
    
    @Mock
    private UserMapper userMapper;
    
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    
    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;
    
    @Test
    @DisplayName("Debería ejecutar el caso de uso de registro exitosamente")
    void shouldExecuteRegisterUseCaseSuccessfully() {
        // Given
        RegisterUserRequest request = RegisterUserRequest.builder()
                .name("Juan Rodriguez")
                .email("juan@rodriguez.org")
                .password("Hunter22")
                .phones(new ArrayList<>())
                .build();
        
        User userToRegister = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phones(new ArrayList<>())
                .build();
        
        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        
        User registeredUser = User.builder()
                .id(userId)
                .name(request.getName())
                .email(request.getEmail())
                .phones(new ArrayList<>())
                .created(now)
                .modified(now)
                .lastLogin(now)
                .isactive(true)
                .build();
        
        String token = "generated.jwt.token";
        
        UserResponse expectedResponse = UserResponse.builder()
                .id(userId)
                .name(request.getName())
                .email(request.getEmail())
                .phones(new ArrayList<>())
                .created(now)
                .modified(now)
                .lastLogin(now)
                .token(token)
                .isactive(true)
                .build();
        
        when(userMapper.toEntity(any(RegisterUserRequest.class))).thenReturn(userToRegister);
        when(userService.registerUser(any(User.class), anyString())).thenReturn(registeredUser);
        when(jwtTokenProvider.generateToken(anyString())).thenReturn(token);
        doNothing().when(userService).updateUserToken(any(User.class), anyString());
        when(userMapper.toResponse(any(User.class))).thenReturn(expectedResponse);
        
        // When
        UserResponse response = registerUserUseCase.execute(request);
        
        // Then
        assertNotNull(response);
        assertEquals(userId, response.getId());
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(token, response.getToken());
        assertTrue(response.getIsactive());
        
        verify(userMapper, times(1)).toEntity(request);
        verify(userService, times(1)).registerUser(userToRegister, request.getPassword());
        verify(jwtTokenProvider, times(1)).generateToken(registeredUser.getEmail());
        verify(userService, times(1)).updateUserToken(registeredUser, token);
        verify(userMapper, times(1)).toResponse(registeredUser);
    }
    
    @Test
    @DisplayName("Debería propagar excepciones del servicio de dominio")
    void shouldPropagateExceptionsFromDomainService() {
        // Given
        RegisterUserRequest request = RegisterUserRequest.builder()
                .name("Juan Rodriguez")
                .email("invalid-email")
                .password("Hunter22")
                .phones(new ArrayList<>())
                .build();
        
        User userToRegister = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
        
        when(userMapper.toEntity(any(RegisterUserRequest.class))).thenReturn(userToRegister);
        when(userService.registerUser(any(User.class), anyString()))
                .thenThrow(new RuntimeException("Email inválido"));
        
        // When & Then
        assertThrows(RuntimeException.class, 
            () -> registerUserUseCase.execute(request));
        
        verify(userMapper, times(1)).toEntity(request);
        verify(userService, times(1)).registerUser(userToRegister, request.getPassword());
        verify(jwtTokenProvider, never()).generateToken(anyString());
        verify(userMapper, never()).toResponse(any());
    }
}
