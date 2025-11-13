package com.example.userapi.domain.service;

import com.example.userapi.domain.exception.BusinessException;
import com.example.userapi.domain.exception.ValidationException;
import com.example.userapi.domain.model.User;
import com.example.userapi.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService - Tests de Lógica de Negocio")
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "emailRegexp", "^[a-z]+@[a-z]+\\.[a-z]{2,}$");
        ReflectionTestUtils.setField(userService, "passwordRegexp", "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d.*\\d)[A-Za-z\\d]{8,}$");
    }
    
    @Test
    @DisplayName("Debería registrar un usuario exitosamente")
    void shouldRegisterUserSuccessfully() {
        // Given
        User user = User.builder()
                .name("Juan Rodriguez")
                .email("juan@rodriguez.org")
                .phones(new ArrayList<>())
                .build();
        
        String rawPassword = "Hunter22";
        String encodedPassword = "$2a$10$encodedPassword";
        
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(java.util.UUID.randomUUID());
            return savedUser;
        });
        
        // When
        User registeredUser = userService.registerUser(user, rawPassword);
        
        // Then
        assertNotNull(registeredUser);
        assertNotNull(registeredUser.getId());
        assertEquals(encodedPassword, registeredUser.getPassword());
        assertTrue(registeredUser.getIsactive());
        
        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(userRepository, times(1)).save(user);
    }
    
    @Test
    @DisplayName("Deberia lanzar excepcion cuando el email es invalido")
    void shouldThrowExceptionWhenEmailIsInvalid() {
        // Given
        User user = User.builder()
                .name("Jorge Marquez")
                .email("invalid-email")
                .build();
        
        // When & Then
        assertThrows(ValidationException.class, 
            () -> userService.registerUser(user, "Hunter22"));
        
        verify(userRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Deberia lanzar excepcion cuando la contraseña es invalida")
    void shouldThrowExceptionWhenPasswordIsInvalid() {
        // Given
        User user = User.builder()
                .name("Jorge Marquez")
                .email("jorge@marquez.org")
                .build();
        
        String invalidPassword = "weak";
        
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        
        // When & Then
        assertThrows(ValidationException.class, 
            () -> userService.registerUser(user, invalidPassword));
        
        verify(userRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Debería lanzar excepción cuando el email ya está registrado")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Given
        User user = User.builder()
                .name("Jorge Marquez")
                .email("jorge@marquez.org")
                .build();
        
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        
        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
            () -> userService.registerUser(user, "Hunter22"));
        
        assertEquals("El correo ya registrado", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Debería validar email correctamente")
    void shouldValidateEmailCorrectly() {
        // Valid email
        assertDoesNotThrow(() -> userService.validateEmail("jorge@marquez.org"));
        
        // Invalid emails
        assertThrows(ValidationException.class, 
            () -> userService.validateEmail("invalid"));
        assertThrows(ValidationException.class, 
            () -> userService.validateEmail("Jorge@Marquez.org")); // Uppercase not allowed
        assertThrows(ValidationException.class, 
            () -> userService.validateEmail(""));
        assertThrows(ValidationException.class, 
            () -> userService.validateEmail(null));
    }
    
    @Test
    @DisplayName("Debería validar contraseña correctamente")
    void shouldValidatePasswordCorrectly() {
        // Valid password
        assertDoesNotThrow(() -> userService.validatePassword("Hunter22"));
        
        // Invalid passwords
        assertThrows(ValidationException.class, 
            () -> userService.validatePassword("weak"));
        assertThrows(ValidationException.class, 
            () -> userService.validatePassword("NoNumbers"));
        assertThrows(ValidationException.class, 
            () -> userService.validatePassword(""));
        assertThrows(ValidationException.class, 
            () -> userService.validatePassword(null));
    }
    
    @Test
    @DisplayName("Debería actualizar el token del usuario")
    void shouldUpdateUserToken() {
        // Given
        User user = User.builder()
                .name("Jorge Marquez")
                .email("jorge@marquez.org")
                .build();
        
        String newToken = "newJwtToken";
        
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        // When
        userService.updateUserToken(user, newToken);
        
        // Then
        assertEquals(newToken, user.getToken());
        verify(userRepository, times(1)).save(user);
    }
}
