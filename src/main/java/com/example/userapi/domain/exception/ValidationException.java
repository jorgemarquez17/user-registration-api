package com.example.userapi.domain.exception;

/**
 * Excepcion de dominio para errores de validacion
 */
public class ValidationException extends RuntimeException {
    
    public ValidationException(String message) {
        super(message);
    }
}
