package com.temperature.api.controller;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.temperature.api.exception.InvalidTemperatureException;
import com.temperature.api.model.TemperatureConversionRequest;
import com.temperature.api.model.TemperatureConversionResponse;
import com.temperature.api.service.TemperatureConversionService;

/**
 * Pruebas unitarias para TemperatureController.
 * 
 * Utiliza @WebMvcTest para probar únicamente la capa web,
 * mockeando las dependencias del servicio.
 */
@WebMvcTest(TemperatureController.class)
@DisplayName("TemperatureController Tests")
class TemperatureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TemperatureConversionService conversionService;

    @Autowired
    private ObjectMapper objectMapper;

    private TemperatureConversionResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleResponse = new TemperatureConversionResponse(
                25.0, "Celsius", 77.0, "Fahrenheit", "F = (C × 9/5) + 32"
        );
    }

    @Nested
    @DisplayName("GET Endpoints - Path Variables")
    class GetEndpointsTests {

        @Test
        @DisplayName("GET /api/temperature/celsius-to-fahrenheit/{celsius} - Success")
        void shouldConvertCelsiusToFahrenheitViaPathVariable() throws Exception {
            // Given
            when(conversionService.celsiusToFahrenheit(25.0)).thenReturn(sampleResponse);

            // When & Then
            mockMvc.perform(get("/api/temperature/celsius-to-fahrenheit/25.0"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.originalValue", is(25.0)))
                    .andExpect(jsonPath("$.originalUnit", is("Celsius")))
                    .andExpect(jsonPath("$.convertedValue", is(77.0)))
                    .andExpect(jsonPath("$.convertedUnit", is("Fahrenheit")))
                    .andExpect(jsonPath("$.formula", is("F = (C × 9/5) + 32")))
                    .andExpect(jsonPath("$.timestamp", notNullValue()));

            verify(conversionService).celsiusToFahrenheit(25.0);
        }

        @Test
        @DisplayName("GET /api/temperature/fahrenheit-to-celsius/{fahrenheit} - Success")
        void shouldConvertFahrenheitToCelsiusViaPathVariable() throws Exception {
            // Given
            TemperatureConversionResponse response = new TemperatureConversionResponse(
                    77.0, "Fahrenheit", 25.0, "Celsius", "C = (F - 32) × 5/9"
            );
            when(conversionService.fahrenheitToCelsius(77.0)).thenReturn(response);

            // When & Then
            mockMvc.perform(get("/api/temperature/fahrenheit-to-celsius/77.0"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.originalValue", is(77.0)))
                    .andExpect(jsonPath("$.convertedValue", is(25.0)));

            verify(conversionService).fahrenheitToCelsius(77.0);
        }

        @Test
        @DisplayName("GET with invalid temperature should return 400")
        void shouldReturnBadRequestForInvalidTemperature() throws Exception {
            // Given
            when(conversionService.celsiusToFahrenheit(anyDouble()))
                    .thenThrow(new InvalidTemperatureException(-500.0, "°C", "Invalid temperature"));

            // When & Then
            mockMvc.perform(get("/api/temperature/celsius-to-fahrenheit/-500.0"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", containsString("Invalid temperature")))
                    .andExpect(jsonPath("$.status", is(400)));
        }

        @Test
        @DisplayName("GET with non-numeric value should return 400")
        void shouldReturnBadRequestForNonNumericValue() throws Exception {
            // When & Then
            mockMvc.perform(get("/api/temperature/celsius-to-fahrenheit/abc"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST Endpoints - JSON Body")
    class PostEndpointsTests {

        @Test
        @DisplayName("POST /api/temperature/celsius-to-fahrenheit - Success")
        void shouldConvertCelsiusToFahrenheitViaPost() throws Exception {
            // Given
            TemperatureConversionRequest request = new TemperatureConversionRequest(25.0);
            when(conversionService.celsiusToFahrenheit(25.0)).thenReturn(sampleResponse);

            // When & Then
            mockMvc.perform(post("/api/temperature/celsius-to-fahrenheit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.originalValue", is(25.0)))
                    .andExpect(jsonPath("$.convertedValue", is(77.0)));

            verify(conversionService).celsiusToFahrenheit(25.0);
        }

        @Test
        @DisplayName("POST /api/temperature/fahrenheit-to-celsius - Success")
        void shouldConvertFahrenheitToCelsiusViaPost() throws Exception {
            // Given
            TemperatureConversionRequest request = new TemperatureConversionRequest(77.0);
            TemperatureConversionResponse response = new TemperatureConversionResponse(
                    77.0, "Fahrenheit", 25.0, "Celsius", "C = (F - 32) × 5/9"
            );
            when(conversionService.fahrenheitToCelsius(77.0)).thenReturn(response);

            // When & Then
            mockMvc.perform(post("/api/temperature/fahrenheit-to-celsius")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.originalValue", is(77.0)))
                    .andExpect(jsonPath("$.convertedValue", is(25.0)));
        }

        @Test
        @DisplayName("POST with null value should return 400")
        void shouldReturnBadRequestForNullValue() throws Exception {
            // Given
            TemperatureConversionRequest request = new TemperatureConversionRequest(null);

            // When & Then
            mockMvc.perform(post("/api/temperature/celsius-to-fahrenheit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.validationErrors", notNullValue()));
        }

        @Test
        @DisplayName("POST with value below minimum should return 400")
        void shouldReturnBadRequestForValueBelowMinimum() throws Exception {
            // Given
            TemperatureConversionRequest request = new TemperatureConversionRequest(-300.0);

            // When & Then
            mockMvc.perform(post("/api/temperature/celsius-to-fahrenheit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST with empty body should return 400")
        void shouldReturnBadRequestForEmptyBody() throws Exception {
            // When & Then
            mockMvc.perform(post("/api/temperature/celsius-to-fahrenheit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Health and Info Endpoints")
    class HealthInfoEndpointsTests {

        @Test
        @DisplayName("GET /api/temperature/health - Should return healthy status")
        void shouldReturnHealthyStatus() throws Exception {
            // Given
            when(conversionService.celsiusToFahrenheit(0.0))
                    .thenReturn(new TemperatureConversionResponse(0.0, "Celsius", 32.0, "Fahrenheit", "F = (C × 9/5) + 32"));

            // When & Then
            mockMvc.perform(get("/api/temperature/health"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", is("UP")))
                    .andExpect(jsonPath("$.service", is("Temperature Conversion API")))
                    .andExpect(jsonPath("$.serviceCheck", is("OK")))
                    .andExpect(jsonPath("$.timestamp", notNullValue()));
        }

        @Test
        @DisplayName("GET /api/temperature/health - Should return unhealthy status when service fails")
        void shouldReturnUnhealthyStatusWhenServiceFails() throws Exception {
            // Given
            when(conversionService.celsiusToFahrenheit(0.0))
                    .thenThrow(new RuntimeException("Service failure"));

            // When & Then
            mockMvc.perform(get("/api/temperature/health"))
                    .andDo(print())
                    .andExpect(status().isServiceUnavailable())
                    .andExpect(jsonPath("$.status", is("UP")))
                    .andExpect(jsonPath("$.serviceCheck", is("ERROR")))
                    .andExpect(jsonPath("$.error", containsString("Service failure")));
        }

        @Test
        @DisplayName("GET /api/temperature/info - Should return API information")
        void shouldReturnApiInformation() throws Exception {
            // Given
            Map<String, Double> constants = new HashMap<>();
            constants.put("ABSOLUTE_ZERO_CELSIUS", -273.15);
            constants.put("ABSOLUTE_ZERO_FAHRENHEIT", -459.67);
            when(conversionService.getConversionConstants()).thenReturn(constants);

            // When & Then
            mockMvc.perform(get("/api/temperature/info"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Temperature Conversion API")))
                    .andExpect(jsonPath("$.version", is("1.0.0")))
                    .andExpect(jsonPath("$.formulas", notNullValue()))
                    .andExpect(jsonPath("$.constants", notNullValue()))
                    .andExpect(jsonPath("$.endpoints", notNullValue()));
        }
    }

    @Nested
    @DisplayName("CORS Configuration Tests")
    class CorsTests {

        @Test
        @DisplayName("Should handle CORS preflight request")
        void shouldHandleCorsPreflight() throws Exception {
            // When & Then
            mockMvc.perform(options("/api/temperature/celsius-to-fahrenheit/25.0")
                            .header("Origin", "http://localhost:3000")
                            .header("Access-Control-Request-Method", "GET"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(header().exists("Access-Control-Allow-Origin"));
        }
    }
}
