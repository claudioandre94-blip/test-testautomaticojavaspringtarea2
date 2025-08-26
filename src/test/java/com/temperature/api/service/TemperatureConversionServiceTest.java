package com.temperature.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import com.temperature.api.exception.InvalidTemperatureException;
import com.temperature.api.model.TemperatureConversionResponse;

/**
 * Pruebas unitarias para TemperatureConversionService.
 * 
 * Esta clase contiene pruebas comprehensivas para todas las funcionalidades
 * del servicio de conversión de temperaturas, incluyendo casos válidos,
 * casos límite y manejo de errores.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TemperatureConversionService Tests")
class TemperatureConversionServiceTest {

    private TemperatureConversionService service;

    @BeforeEach
    void setUp() {
        service = new TemperatureConversionService();
    }

    @Nested
    @DisplayName("Celsius to Fahrenheit Conversion Tests")
    class CelsiusToFahrenheitTests {

        @Test
        @DisplayName("Should convert 0°C to 32°F")
        void shouldConvertZeroCelsiusToThirtyTwoFahrenheit() {
            // Given
            Double celsius = 0.0;

            // When
            TemperatureConversionResponse response = service.celsiusToFahrenheit(celsius);

            // Then
            assertNotNull(response);
            assertEquals(0.0, response.getOriginalValue());
            assertEquals("Celsius", response.getOriginalUnit());
            assertEquals(32.0, response.getConvertedValue());
            assertEquals("Fahrenheit", response.getConvertedUnit());
            assertEquals("F = (C × 9/5) + 32", response.getFormula());
            assertTrue(response.getTimestamp() > 0);
        }

        @Test
        @DisplayName("Should convert 100°C to 212°F (boiling point)")
        void shouldConvertBoilingPointCelsiusToFahrenheit() {
            // Given
            Double celsius = 100.0;

            // When
            TemperatureConversionResponse response = service.celsiusToFahrenheit(celsius);

            // Then
            assertEquals(100.0, response.getOriginalValue());
            assertEquals(212.0, response.getConvertedValue());
        }

        @Test
        @DisplayName("Should convert 37°C to 98.6°F (body temperature)")
        void shouldConvertBodyTemperatureCelsiusToFahrenheit() {
            // Given
            Double celsius = 37.0;

            // When
            TemperatureConversionResponse response = service.celsiusToFahrenheit(celsius);

            // Then
            assertEquals(37.0, response.getOriginalValue());
            assertEquals(98.6, response.getConvertedValue());
        }

        @Test
        @DisplayName("Should convert -40°C to -40°F (special case where scales meet)")
        void shouldConvertMinusFortySpecialCase() {
            // Given
            Double celsius = -40.0;

            // When
            TemperatureConversionResponse response = service.celsiusToFahrenheit(celsius);

            // Then
            assertEquals(-40.0, response.getOriginalValue());
            assertEquals(-40.0, response.getConvertedValue());
        }

        @ParameterizedTest
        @ValueSource(doubles = {-50.0, 25.5, 1000.0, -200.0})
        @DisplayName("Should convert various valid temperatures")
        void shouldConvertVariousValidTemperatures(Double celsius) {
            // When
            TemperatureConversionResponse response = service.celsiusToFahrenheit(celsius);

            // Then
            assertNotNull(response);
            assertEquals(celsius, response.getOriginalValue());
            assertNotNull(response.getConvertedValue());

            // Verify formula manually
            Double expectedFahrenheit = (celsius * 9.0 / 5.0) + 32.0;
            assertEquals(expectedFahrenheit, response.getConvertedValue(), 0.01);
        }

        @Test
        @DisplayName("Should throw exception for null input")
        void shouldThrowExceptionForNullInput() {
            // When & Then
            InvalidTemperatureException exception = assertThrows(
                    InvalidTemperatureException.class,
                    () -> service.celsiusToFahrenheit(null)
            );
            assertTrue(exception.getMessage().contains("no puede ser nulo"));
        }

        @Test
        @DisplayName("Should throw exception for temperature below absolute zero")
        void shouldThrowExceptionForBelowAbsoluteZero() {
            // Given
            Double belowAbsoluteZero = -300.0;

            // When & Then
            InvalidTemperatureException exception = assertThrows(
                    InvalidTemperatureException.class,
                    () -> service.celsiusToFahrenheit(belowAbsoluteZero)
            );

            assertTrue(exception.getMessage().contains("cero absoluto"));
            assertEquals(belowAbsoluteZero, exception.getInvalidValue());
        }

        @Test
        @DisplayName("Should throw exception for temperature above maximum limit")
        void shouldThrowExceptionForAboveMaximum() {
            // Given
            Double aboveMaximum = 15000.0;

            // When & Then
            InvalidTemperatureException exception = assertThrows(
                    InvalidTemperatureException.class,
                    () -> service.celsiusToFahrenheit(aboveMaximum)
            );

            assertTrue(exception.getMessage().contains("excede el límite"));
            assertEquals(aboveMaximum, exception.getInvalidValue());
        }

        @Test
        @DisplayName("Should throw exception for NaN input")
        void shouldThrowExceptionForNaNInput() {
            // Given
            Double nanValue = Double.NaN;

            // When & Then
            InvalidTemperatureException exception = assertThrows(
                    InvalidTemperatureException.class,
                    () -> service.celsiusToFahrenheit(nanValue)
            );

            assertTrue(exception.getMessage().contains("NaN"));
        }

        @Test
        @DisplayName("Should throw exception for infinite input")
        void shouldThrowExceptionForInfiniteInput() {
            // Given
            Double infiniteValue = Double.POSITIVE_INFINITY;

            // When & Then
            InvalidTemperatureException exception = assertThrows(
                    InvalidTemperatureException.class,
                    () -> service.celsiusToFahrenheit(infiniteValue)
            );
            assertTrue(exception.getMessage().contains("Infinity"));
        }
    }

    @Nested
    @DisplayName("Fahrenheit to Celsius Conversion Tests")
    class FahrenheitToCelsiusTests {

        @Test
        @DisplayName("Should convert 32°F to 0°C")
        void shouldConvertThirtyTwoFahrenheitToZeroCelsius() {
            // Given
            Double fahrenheit = 32.0;

            // When
            TemperatureConversionResponse response = service.fahrenheitToCelsius(fahrenheit);

            // Then
            assertNotNull(response);
            assertEquals(32.0, response.getOriginalValue());
            assertEquals("Fahrenheit", response.getOriginalUnit());
            assertEquals(0.0, response.getConvertedValue());
            assertEquals("Celsius", response.getConvertedUnit());
            assertEquals("C = (F - 32) × 5/9", response.getFormula());
        }

        @Test
        @DisplayName("Should convert 212°F to 100°C (boiling point)")
        void shouldConvertBoilingPointFahrenheitToCelsius() {
            // Given
            Double fahrenheit = 212.0;

            // When
            TemperatureConversionResponse response = service.fahrenheitToCelsius(fahrenheit);

            // Then
            assertEquals(212.0, response.getOriginalValue());
            assertEquals(100.0, response.getConvertedValue());
        }

        @Test
        @DisplayName("Should convert 98.6°F to 37°C (body temperature)")
        void shouldConvertBodyTemperatureFahrenheitToCelsius() {
            // Given
            Double fahrenheit = 98.6;

            // When
            TemperatureConversionResponse response = service.fahrenheitToCelsius(fahrenheit);

            // Then
            assertEquals(98.6, response.getOriginalValue());
            assertEquals(37.0, response.getConvertedValue());
        }

        @Test
        @DisplayName("Should throw exception for temperature below absolute zero in Fahrenheit")
        void shouldThrowExceptionForBelowAbsoluteZeroFahrenheit() {
            // Given
            Double belowAbsoluteZero = -500.0;

            // When & Then
            InvalidTemperatureException exception = assertThrows(
                    InvalidTemperatureException.class,
                    () -> service.fahrenheitToCelsius(belowAbsoluteZero)
            );

            assertTrue(exception.getMessage().contains("cero absoluto"));
        }
    }

    @Nested
    @DisplayName("Temperature Context and Utility Tests")
    class TemperatureContextTests {

        @Test
        @DisplayName("Should identify freezing point")
        void shouldIdentifyFreezingPoint() {
            assertTrue(service.isFreezingPoint(0.0));
            assertTrue(service.isFreezingPoint(-0.001)); // Within tolerance
            assertFalse(service.isFreezingPoint(0.1));
            assertFalse(service.isFreezingPoint(null));
        }

        @Test
        @DisplayName("Should identify boiling point")
        void shouldIdentifyBoilingPoint() {
            assertTrue(service.isBoilingPoint(100.0));
            assertTrue(service.isBoilingPoint(99.999)); // Within tolerance
            assertFalse(service.isBoilingPoint(99.0));
            assertFalse(service.isBoilingPoint(null));
        }

        @Test
        @DisplayName("Should provide appropriate temperature contexts")
        void shouldProvideAppropriateTemperatureContexts() {
            assertEquals("Punto de congelación del agua", 
                    service.getTemperatureContext(0.0));
            assertEquals("Punto de ebullición del agua (a nivel del mar)", 
                    service.getTemperatureContext(100.0));
            assertEquals("Temperatura corporal normal", 
                    service.getTemperatureContext(37.0));
            assertEquals("Por debajo del punto de congelación", 
                    service.getTemperatureContext(-10.0));
            assertEquals("Por encima del punto de ebullición", 
                    service.getTemperatureContext(150.0));
            assertEquals("Temperatura ambiente confortable", 
                    service.getTemperatureContext(22.0));
            assertEquals("Temperatura no válida", 
                    service.getTemperatureContext(null));
        }

        @Test
        @DisplayName("Should provide conversion constants")
        void shouldProvideConversionConstants() {
            // When
            var constants = service.getConversionConstants();

            // Then
            assertNotNull(constants);
            assertEquals(-273.15, constants.get("ABSOLUTE_ZERO_CELSIUS"));
            assertEquals(-459.67, constants.get("ABSOLUTE_ZERO_FAHRENHEIT"));
            assertEquals(10000.0, constants.get("MAX_REASONABLE_TEMPERATURE"));
        }
    }
}
