package com.example.userapi.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Pattern - Response estandar para mensajes de error
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta de error estandar")
public class ErrorResponse {
    
    @Schema(description = "Mensaje de error", example = "El correo ya registrado")
    @JsonProperty("mensaje")
    private String mensaje;
}
