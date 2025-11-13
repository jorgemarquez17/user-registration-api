package com.example.userapi.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtTokenProvider - Tests de Generación y Validación de JWT")
class JwtTokenProviderTest {
    
    private JwtTokenProvider jwtTokenProvider;
    
    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", 
            "miClaveSecretaSuperSeguraParaJWT2025DebeSerLargaYCompleja");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationMs", 86400000L);
    }
    
    @Test
    @DisplayName("Deberia generar un token JWT valido")
    void shouldGenerateValidJwtToken() {
        // Given
        String email = "jorge@marquez.org";
        
        // When
        String token = jwtTokenProvider.generateToken(email);
        
        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT tiene 3 partes separadas por punto
    }
    
    @Test
    @DisplayName("Deberia extraer el email del token correctamente")
    void shouldExtractEmailFromToken() {
        // Given
        String email = "jorge@marquez.org";
        String token = jwtTokenProvider.generateToken(email);
        
        // When
        String extractedEmail = jwtTokenProvider.getEmailFromToken(token);
        
        // Then
        assertEquals(email, extractedEmail);
    }
    
    @Test
    @DisplayName("Deberia validar token correctamente")
    void shouldValidateTokenCorrectly() {
        // Given
        String email = "jorge@marquez.org";
        String token = jwtTokenProvider.generateToken(email);
        
        // When
        boolean isValid = jwtTokenProvider.validateToken(token);
        
        // Then
        assertTrue(isValid);
    }
    
    @Test
    @DisplayName("Deberia rechazar token invalido")
    void shouldRejectInvalidToken() {
        // Given
        String invalidToken = "invalid.jwt.token";
        
        // When
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);
        
        // Then
        assertFalse(isValid);
    }
    
    @Test
    @DisplayName("Deberia rechazar token malformado")
    void shouldRejectMalformedToken() {
        // Given
        String malformedToken = "malformed-token";
        
        // When
        boolean isValid = jwtTokenProvider.validateToken(malformedToken);
        
        // Then
        assertFalse(isValid);
    }
}
