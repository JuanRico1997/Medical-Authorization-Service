package com.meditrack.authorization.infrastructure.config;

import com.meditrack.authorization.domain.exceptions.*;
import com.meditrack.authorization.infrastructure.adapters.in.rest.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones
 * Captura todas las excepciones y las convierte en respuestas HTTP estandarizadas
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones de recurso no encontrado (404)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Maneja excepciones de reglas de negocio (400)
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(
            BusinessRuleException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Business Rule Violation",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Maneja excepciones de acceso no autorizado (403)
     */
    @ExceptionHandler({UnauthorizedAccessException.class, AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccess(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                ex.getMessage() != null ? ex.getMessage() : "No tienes permiso para acceder a este recurso",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    /**
     * Maneja excepciones de recurso duplicado (409)
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(
            DuplicateResourceException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Maneja excepciones de servicios externos (502)
     */
    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleExternalService(
            ExternalServiceException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_GATEWAY.value(),
                "Bad Gateway",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
    }

    /**
     * Maneja excepciones de validación de Jakarta Bean Validation (400)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<ErrorResponse.ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorResponse.ValidationError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Los datos enviados no son válidos",
                request.getRequestURI(),
                validationErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Maneja excepciones de IllegalArgumentException (400)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Maneja excepciones de IllegalStateException (409)
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(
            IllegalStateException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Maneja todas las demás excepciones no capturadas (500)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        // Log del error para debugging
        System.err.println("Error no manejado: " + ex.getClass().getName());
        ex.printStackTrace();

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ha ocurrido un error interno en el servidor. Por favor contacta al administrador.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}