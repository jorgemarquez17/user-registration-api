package com.example.userapi.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Pattern - Objeto de transferencia de datos para teléfonos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Informacion del teléfono")
public class PhoneDTO {
    
    @Schema(description = "Numero de telefono", example = "1234567")
    @JsonProperty("number")
    private String number;
    
    @Schema(description = "Codigo de ciudad", example = "1")
    @JsonProperty("citycode")
    private String citycode;
    
    @Schema(description = "Codigo de pais", example = "57")
    @JsonProperty("contrycode")
    private String contrycode;
}
