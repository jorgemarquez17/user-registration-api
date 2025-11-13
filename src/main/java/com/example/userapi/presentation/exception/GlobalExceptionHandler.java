package com.example.userapi.presentation.exception;

import com.example.userapi.application.dto.ErrorResponse;
import com.example.userapi.domain.exception.BusinessException;
import com.example.userapi.domain.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Strategy Pattern - Manejador global de excepciones.
 * Centraliza el manejo de errores y asegura respuestas consistentes.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    /**
     * Maneja excepciones de validación del dominio
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        log.warn("Error de validacion: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .mensaje(ex.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
    
    /**
     * Maneja excepciones de logica de negocio
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        log.warn("Error de negocio: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .mensaje(ex.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
    
    /**
     * Maneja errores de validacion de Bean Validation (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        log.warn("Error de validacion de request: {}", errorMessage);
        
        ErrorResponse error = ErrorResponse.builder()
                .mensaje(errorMessage)
                .build();
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
    
    /**
     * Maneja excepciones genericas no controladas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Error interno del servidor: ", ex);
        ErrorResponse error = ErrorResponse.builder()
                .mensaje("Error interno del servidor")
                .build();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
