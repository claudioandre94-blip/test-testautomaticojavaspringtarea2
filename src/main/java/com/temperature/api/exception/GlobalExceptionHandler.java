package com.temperature.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la API de conversión de temperaturas.
 * 
 * Esta clase centraliza el manejo de todas las excepciones de la aplicación,
 * proporcionando respuestas HTTP consistentes y mensajes de error informativos.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones específicas de conversión de temperatura.
     *
     * @param ex      excepción de conversión
     * @param request petición HTTP que causó el error
     * @return respuesta de error estructurada
     */
    @ExceptionHandler(TemperatureConversionException.class)
    public ResponseEntity<Map<String, Object>> handleTemperatureConversionException(
            TemperatureConversionException ex, HttpServletRequest request) {

        Map<String, Object> errorResponse = createBaseErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI()
        );

        errorResponse.put("errorCode", ex.getErrorCode());
        if (ex.getErrorArgs().length > 0) {
            errorResponse.put("errorArgs", ex.getErrorArgs());
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones de temperaturas inválidas.
     *
     * @param ex      excepción de temperatura inválida
     * @param request petición HTTP que causó el error
     * @return respuesta de error estructurada
     */
    @ExceptionHandler(InvalidTemperatureException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidTemperatureException(
            InvalidTemperatureException ex, HttpServletRequest request) {

        Map<String, Object> errorResponse = createBaseErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI()
        );

        errorResponse.put("errorCode", ex.getErrorCode());
        errorResponse.put("invalidValue", ex.getInvalidValue());
        errorResponse.put("unit", ex.getUnit());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja errores de validación de Bean Validation (JSR-303).
     *
     * @param ex      excepción de validación
     * @param request petición HTTP que causó el error
     * @return respuesta de error con detalles de validación
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, Object> errorResponse = createBaseErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Datos de entrada inválidos",
                request.getRequestURI()
        );

        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> validationErrors = new HashMap<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        errorResponse.put("validationErrors", validationErrors);
        errorResponse.put("errorCode", "VALIDATION_ERROR");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja errores de tipo de argumento incorrecto (ej: texto en lugar de número).
     *
     * @param ex      excepción de tipo de argumento
     * @param request petición HTTP que causó el error
     * @return respuesta de error estructurada
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String message = String.format("El parámetro '%s' debe ser de tipo %s, pero se recibió: '%s'",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido",
                ex.getValue());

        Map<String, Object> errorResponse = createBaseErrorResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request.getRequestURI()
        );

        errorResponse.put("errorCode", "TYPE_MISMATCH_ERROR");
        errorResponse.put("parameterName", ex.getName());
        errorResponse.put("providedValue", ex.getValue());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones genéricas no capturadas por otros manejadores.
     *
     * @param ex      excepción genérica
     * @param request petición HTTP que causó el error
     * @return respuesta de error genérica
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex, HttpServletRequest request) {

        Map<String, Object> errorResponse = createBaseErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                request.getRequestURI()
        );

        errorResponse.put("errorCode", "INTERNAL_SERVER_ERROR");

        // En ambiente de desarrollo, incluir detalles técnicos
        // En producción, estos detalles no deberían exponerse
        errorResponse.put("technicalMessage", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Crea la estructura base de respuesta de error.
     *
     * @param status  código de estado HTTP
     * @param message mensaje de error
     * @param path    ruta de la petición que causó el error
     * @return mapa con la estructura base del error
     */
    private Map<String, Object> createBaseErrorResponse(HttpStatus status, String message, String path) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);
        errorResponse.put("path", path);
        return errorResponse;
    }
}
