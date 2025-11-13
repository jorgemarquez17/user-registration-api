package com.example.userapi.presentation.controller;

import com.example.userapi.application.dto.ErrorResponse;
import com.example.userapi.application.dto.RegisterUserRequest;
import com.example.userapi.application.dto.UserResponse;
import com.example.userapi.application.usecase.RegisterUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller Pattern - Controlador REST para operaciones de usuarios.
 * Capa de presentacion que expone los endpoints de la API.
 */
@RestController
@RequestMapping("/api/users")
@Slf4j
@Tag(name = "Usuarios", description = "API de gestion de usuarios")
public class UserController {
    
    private final RegisterUserUseCase registerUserUseCase;

    public UserController(RegisterUserUseCase registerUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
    }

    /**
     * Endpoint para registrar un nuevo usuario
     * 
     * @param request Datos del usuario a registrar
     * @return UserResponse con los datos del usuario registrado
     */
    @PostMapping(
        value = "/register",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Crea un nuevo usuario en el sistema y retorna su información con un token JWT"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuario creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada invalidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "El correo ya esta registrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody RegisterUserRequest request) {
        
        log.info("Recibida peticion de registro para email: {}", request.getEmail());
        
        UserResponse response = registerUserUseCase.execute(request);
        
        log.info("Usuario registrado exitosamente con ID: {}", response.getId());
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
