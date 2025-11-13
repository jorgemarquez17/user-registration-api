package com.example.userapi.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO Pattern - Request para el registro de usuarios
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos para registrar un nuevo usuario")
public class RegisterUserRequest {
    
    @Schema(description = "Nombre completo del usuario", example = "Jorge Marquez", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre es obligatorio")
    @JsonProperty("name")
    private String name;
    
    @Schema(description = "Correo electronico del usuario", example = "jorge@marquez.org", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El correo es obligatorio")
    @JsonProperty("email")
    private String email;
    
    @Schema(description = "Contraseña del usuario", example = "Hunter22", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La contraseña es obligatoria")
    @JsonProperty("password")
    private String password;
    
    @Schema(description = "Lista de telefonos del usuario")
    @NotNull(message = "La lista de telefonos no puede ser null")
    @JsonProperty("phones")
    @Builder.Default
    private List<PhoneDTO> phones = new ArrayList<>();
}
