package com.example.userapi.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO Pattern - Response con la información del usuario registrado
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta con los datos del usuario registrado")
public class UserResponse {
    
    @Schema(description = "ID unico del usuario", example = "550e8400-e29b-41d4-a716-446655440000")
    @JsonProperty("id")
    private UUID id;
    
    @Schema(description = "Nombre del usuario", example = "Jorge Marquez")
    @JsonProperty("name")
    private String name;
    
    @Schema(description = "Correo electronico del usuario", example = "jorge@marquez.org")
    @JsonProperty("email")
    private String email;
    
    @Schema(description = "Lista de telefonos del usuario")
    @JsonProperty("phones")
    @Builder.Default
    private List<PhoneDTO> phones = new ArrayList<>();
    
    @Schema(description = "Fecha de creacion del usuario", example = "2025-11-12T10:30:00")
    @JsonProperty("created")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;
    
    @Schema(description = "Fecha de ultima modificacion", example = "2025-11-12T10:30:00")
    @JsonProperty("modified")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modified;
    
    @Schema(description = "Fecha del ultimo login", example = "2025-11-12T10:30:00")
    @JsonProperty("last_login")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastLogin;
    
    @Schema(description = "Token JWT de acceso")
    @JsonProperty("token")
    private String token;
    
    @Schema(description = "Indica si el usuario esta activo", example = "true")
    @JsonProperty("isactive")
    private Boolean isactive;
}
