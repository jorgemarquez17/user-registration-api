package com.example.userapi.presentation.controller;

import com.example.userapi.application.dto.PhoneDTO;
import com.example.userapi.application.dto.RegisterUserRequest;
import com.example.userapi.application.dto.UserResponse;
import com.example.userapi.application.usecase.RegisterUserUseCase;
import com.example.userapi.domain.exception.BusinessException;
import com.example.userapi.domain.exception.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("UserController - Tests de Integracion")
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RegisterUserUseCase registerUserUseCase;
    
    @Test
    @DisplayName("POST /api/users/register - Deberia registrar usuario exitosamente")
    void shouldRegisterUserSuccessfully() throws Exception {
        // Given
        PhoneDTO phone = PhoneDTO.builder()
                .number("1234567")
                .citycode("1")
                .contrycode("57")
                .build();
        
        RegisterUserRequest request = RegisterUserRequest.builder()
                .name("Jorge Marquez")
                .email("jorge@marquez.org")
                .password("Hunter22")
                .phones(List.of(phone))
                .build();
        
        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        
        UserResponse response = UserResponse.builder()
                .id(userId)
                .name(request.getName())
                .email(request.getEmail())
                .phones(List.of(phone))
                .created(now)
                .modified(now)
                .lastLogin(now)
                .token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                .isactive(true)
                .build();
        
        when(registerUserUseCase.execute(any(RegisterUserRequest.class)))
                .thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.isactive").value(true))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.modified").exists())
                .andExpect(jsonPath("$.last_login").exists());
    }
    
    @Test
    @DisplayName("POST /api/users/register - Deberia retornar 400 cuando faltan campos obligatorios")
    void shouldReturn400WhenRequiredFieldsAreMissing() throws Exception {
        // Given
        RegisterUserRequest request = RegisterUserRequest.builder()
                .name("")
                .email("")
                .password("")
                .phones(new ArrayList<>())
                .build();
        
        // When & Then
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").exists());
    }
    
    @Test
    @DisplayName("POST /api/users/register - Deberia retornar 400 cuando el email es invalido")
    void shouldReturn400WhenEmailIsInvalid() throws Exception {
        // Given
        RegisterUserRequest request = RegisterUserRequest.builder()
                .name("Jorge Marquez")
                .email("invalid-email")
                .password("Hunter22")
                .phones(new ArrayList<>())
                .build();
        
        when(registerUserUseCase.execute(any(RegisterUserRequest.class)))
                .thenThrow(new ValidationException("El formato del correo es invalido"));
        
        // When & Then
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").value("El formato del correo es invalido"));
    }
    
    @Test
    @DisplayName("POST /api/users/register - Deberia retornar 409 cuando el email ya existe")
    void shouldReturn409WhenEmailAlreadyExists() throws Exception {
        // Given
        RegisterUserRequest request = RegisterUserRequest.builder()
                .name("Jorge Marquez")
                .email("jorge@marquez.org")
                .password("Hunter22")
                .phones(new ArrayList<>())
                .build();
        
        when(registerUserUseCase.execute(any(RegisterUserRequest.class)))
                .thenThrow(new BusinessException("El correo ya registrado"));
        
        // When & Then
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").value("El correo ya registrado"));
    }
    
    @Test
    @DisplayName("POST /api/users/register - Deberia retornar 500 cuando ocurre error interno")
    void shouldReturn500WhenInternalErrorOccurs() throws Exception {
        // Given
        RegisterUserRequest request = RegisterUserRequest.builder()
                .name("Jorge Marquez")
                .email("jorge@marquez.org")
                .password("Hunter22")
                .phones(new ArrayList<>())
                .build();
        
        when(registerUserUseCase.execute(any(RegisterUserRequest.class)))
                .thenThrow(new RuntimeException("Database connection error"));
        
        // When & Then
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").value("Error interno del servidor"));
    }
}
