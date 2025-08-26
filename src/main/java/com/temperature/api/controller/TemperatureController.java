package com.temperature.api.controller;

import com.temperature.api.model.TemperatureConversionRequest;
import com.temperature.api.model.TemperatureConversionResponse;
import com.temperature.api.service.TemperatureConversionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para las operaciones de conversión de temperatura.
 * 
 * Este controlador expone endpoints para convertir temperaturas entre
 * Celsius y Fahrenheit, proporcionando una API RESTful completa con
 * documentación OpenAPI/Swagger.
 * 
 * Endpoints disponibles:
 * - GET /api/temperature/celsius-to-fahrenheit/{value}
 * - GET /api/temperature/fahrenheit-to-celsius/{value}
 * - POST /api/temperature/celsius-to-fahrenheit
 * - POST /api/temperature/fahrenheit-to-celsius
 * - GET /api/temperature/health
 */
@RestController
@RequestMapping("/api/temperature")
@Tag(name = "Temperature Conversion", description = "API para conversión de temperaturas entre Celsius y Fahrenheit")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TemperatureController {

    private final TemperatureConversionService conversionService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param conversionService servicio de conversión de temperaturas
     */
    @Autowired
    public TemperatureController(TemperatureConversionService conversionService) {
        this.conversionService = conversionService;
    }

    /**
     * Convierte una temperatura de Celsius a Fahrenheit usando parámetro de path.
     *
     * @param celsius temperatura en grados Celsius
     * @return respuesta con la conversión realizada
     */
    @GetMapping("/celsius-to-fahrenheit/{celsius}")
    @Operation(
            summary = "Convertir Celsius a Fahrenheit",
            description = "Convierte una temperatura de grados Celsius a grados Fahrenheit usando la fórmula F = (C × 9/5) + 32"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversión realizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TemperatureConversionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Temperatura inválida o fuera de rango",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TemperatureConversionResponse> celsiusToFahrenheitPath(
            @Parameter(description = "Temperatura en grados Celsius", example = "25.0", required = true)
            @PathVariable Double celsius) {

        TemperatureConversionResponse response = conversionService.celsiusToFahrenheit(celsius);
        return ResponseEntity.ok(response);
    }

    /**
     * Convierte una temperatura de Fahrenheit a Celsius usando parámetro de path.
     *
     * @param fahrenheit temperatura en grados Fahrenheit
     * @return respuesta con la conversión realizada
     */
    @GetMapping("/fahrenheit-to-celsius/{fahrenheit}")
    @Operation(
            summary = "Convertir Fahrenheit a Celsius",
            description = "Convierte una temperatura de grados Fahrenheit a grados Celsius usando la fórmula C = (F - 32) × 5/9"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversión realizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TemperatureConversionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Temperatura inválida o fuera de rango",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TemperatureConversionResponse> fahrenheitToCelsiusPath(
            @Parameter(description = "Temperatura en grados Fahrenheit", example = "77.0", required = true)
            @PathVariable Double fahrenheit) {

        TemperatureConversionResponse response = conversionService.fahrenheitToCelsius(fahrenheit);
        return ResponseEntity.ok(response);
    }

    /**
     * Convierte una temperatura de Celsius a Fahrenheit usando POST con JSON.
     *
     * @param request objeto con la temperatura a convertir
     * @return respuesta con la conversión realizada
     */
    @PostMapping("/celsius-to-fahrenheit")
    @Operation(
            summary = "Convertir Celsius a Fahrenheit (POST)",
            description = "Convierte una temperatura de grados Celsius a grados Fahrenheit enviando los datos en el cuerpo de la petición"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversión realizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TemperatureConversionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TemperatureConversionResponse> celsiusToFahrenheitPost(
            @Parameter(description = "Datos de temperatura a convertir", required = true)
            @Valid @RequestBody TemperatureConversionRequest request) {

        TemperatureConversionResponse response = conversionService.celsiusToFahrenheit(request.getValue());
        return ResponseEntity.ok(response);
    }

    /**
     * Convierte una temperatura de Fahrenheit a Celsius usando POST con JSON.
     *
     * @param request objeto con la temperatura a convertir
     * @return respuesta con la conversión realizada
     */
    @PostMapping("/fahrenheit-to-celsius")
    @Operation(
            summary = "Convertir Fahrenheit a Celsius (POST)",
            description = "Convierte una temperatura de grados Fahrenheit a grados Celsius enviando los datos en el cuerpo de la petición"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversión realizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TemperatureConversionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TemperatureConversionResponse> fahrenheitToCelsiusPost(
            @Parameter(description = "Datos de temperatura a convertir", required = true)
            @Valid @RequestBody TemperatureConversionRequest request) {

        TemperatureConversionResponse response = conversionService.fahrenheitToCelsius(request.getValue());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de salud para verificar que la API está funcionando.
     *
     * @return información del estado de la API
     */
    @GetMapping("/health")
    @Operation(
            summary = "Verificar estado de la API",
            description = "Endpoint para verificar que la API de conversión de temperaturas está funcionando correctamente"
    )
    @ApiResponse(responseCode = "200", description = "API funcionando correctamente",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "Temperature Conversion API");
        health.put("version", "1.0.0");
        health.put("timestamp", System.currentTimeMillis());

        // Verificar el servicio con una conversión simple
        try {
            conversionService.celsiusToFahrenheit(0.0);
            health.put("serviceCheck", "OK");
        } catch (Exception e) {
            health.put("serviceCheck", "ERROR");
            health.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(health);
        }

        return ResponseEntity.ok(health);
    }

    /**
     * Obtiene información sobre las constantes y límites de la API.
     *
     * @return información sobre los límites de temperatura
     */
    @GetMapping("/info")
    @Operation(
            summary = "Información de la API",
            description = "Obtiene información sobre las constantes, límites y fórmulas utilizadas en la API"
    )
    @ApiResponse(responseCode = "200", description = "Información obtenida exitosamente",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "Temperature Conversion API");
        info.put("description", "API REST para conversión entre Celsius y Fahrenheit");
        info.put("version", "1.0.0");

        Map<String, String> formulas = new HashMap<>();
        formulas.put("celsiusToFahrenheit", "F = (C × 9/5) + 32");
        formulas.put("fahrenheitToCelsius", "C = (F - 32) × 5/9");
        info.put("formulas", formulas);

        info.put("constants", conversionService.getConversionConstants());

        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("GET /api/temperature/celsius-to-fahrenheit/{value}", "Convertir Celsius a Fahrenheit");
        endpoints.put("GET /api/temperature/fahrenheit-to-celsius/{value}", "Convertir Fahrenheit a Celsius");
        endpoints.put("POST /api/temperature/celsius-to-fahrenheit", "Convertir Celsius a Fahrenheit (JSON)");
        endpoints.put("POST /api/temperature/fahrenheit-to-celsius", "Convertir Fahrenheit a Celsius (JSON)");
        endpoints.put("GET /api/temperature/health", "Estado de la API");
        endpoints.put("GET /api/temperature/info", "Información de la API");
        info.put("endpoints", endpoints);

        return ResponseEntity.ok(info);
    }
}
