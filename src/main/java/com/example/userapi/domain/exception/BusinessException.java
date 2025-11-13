package com.example.userapi.domain.exception;

/**
 * Excepcion de dominio para errores de logica de negocio
 */
public class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
}
