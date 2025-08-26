package com.temperature.api.service;

import com.temperature.api.exception.InvalidTemperatureException;
import com.temperature.api.model.TemperatureConversionResponse;
import com.temperature.api.model.TemperatureUnit;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Servicio que contiene la lógica de negocio para conversiones de temperatura.
 * 
 * Esta clase implementa las fórmulas de conversión entre Celsius y Fahrenheit,
 * incluyendo validaciones de entrada y manejo de precisión decimal.
 * 
 * Fórmulas utilizadas:
 * - Celsius a Fahrenheit: F = (C × 9/5) + 32
 * - Fahrenheit a Celsius: C = (F - 32) × 5/9
 */
@Service
public class TemperatureConversionService {

    /**
     * Cero absoluto en Celsius (-273.15°C).
     */
    private static final double ABSOLUTE_ZERO_CELSIUS = -273.15;

    /**
     * Cero absoluto en Fahrenheit (-459.67°F).
     */
    private static final double ABSOLUTE_ZERO_FAHRENHEIT = -459.67;

    /**
     * Límite máximo razonable para temperaturas en la aplicación.
     */
    private static final double MAX_REASONABLE_TEMPERATURE = 10000.0;

    /**
     * Precisión decimal para los resultados (2 decimales).
     */
    private static final int DECIMAL_PRECISION = 2;

    /**
     * Convierte una temperatura de Celsius a Fahrenheit.
     * 
     * Utiliza la fórmula: F = (C × 9/5) + 32
     *
     * @param celsius temperatura en grados Celsius
     * @return objeto respuesta con los detalles de la conversión
     * @throws InvalidTemperatureException si la temperatura está fuera de rangos válidos
     */
    public TemperatureConversionResponse celsiusToFahrenheit(Double celsius) {
        // Validar entrada
        validateTemperature(celsius, TemperatureUnit.CELSIUS);

        // Realizar conversión usando BigDecimal para precisión
        BigDecimal celsiusBD = BigDecimal.valueOf(celsius);
        BigDecimal fahrenheitBD = celsiusBD
                .multiply(BigDecimal.valueOf(9))
                .divide(BigDecimal.valueOf(5), DECIMAL_PRECISION + 2, RoundingMode.HALF_UP)
                .add(BigDecimal.valueOf(32))
                .setScale(DECIMAL_PRECISION, RoundingMode.HALF_UP);

        Double fahrenheit = fahrenheitBD.doubleValue();

        // Crear respuesta
        return new TemperatureConversionResponse(
                celsius,
                TemperatureUnit.CELSIUS.getDisplayName(),
                fahrenheit,
                TemperatureUnit.FAHRENHEIT.getDisplayName(),
                "F = (C × 9/5) + 32"
        );
    }

    /**
     * Convierte una temperatura de Fahrenheit a Celsius.
     * 
     * Utiliza la fórmula: C = (F - 32) × 5/9
     *
     * @param fahrenheit temperatura en grados Fahrenheit
     * @return objeto respuesta con los detalles de la conversión
     * @throws InvalidTemperatureException si la temperatura está fuera de rangos válidos
     */
    public TemperatureConversionResponse fahrenheitToCelsius(Double fahrenheit) {
        // Validar entrada
        validateTemperature(fahrenheit, TemperatureUnit.FAHRENHEIT);

        // Realizar conversión usando BigDecimal para precisión
        BigDecimal fahrenheitBD = BigDecimal.valueOf(fahrenheit);
        BigDecimal celsiusBD = fahrenheitBD
                .subtract(BigDecimal.valueOf(32))
                .multiply(BigDecimal.valueOf(5))
                .divide(BigDecimal.valueOf(9), DECIMAL_PRECISION + 2, RoundingMode.HALF_UP)
                .setScale(DECIMAL_PRECISION, RoundingMode.HALF_UP);

        Double celsius = celsiusBD.doubleValue();

        // Crear respuesta
        return new TemperatureConversionResponse(
                fahrenheit,
                TemperatureUnit.FAHRENHEIT.getDisplayName(),
                celsius,
                TemperatureUnit.CELSIUS.getDisplayName(),
                "C = (F - 32) × 5/9"
        );
    }

    /**
     * Valida que una temperatura esté dentro de rangos físicos válidos.
     *
     * @param temperature valor de temperatura a validar
     * @param unit        unidad de temperatura
     * @throws InvalidTemperatureException si la temperatura es inválida
     */
    private void validateTemperature(Double temperature, TemperatureUnit unit) {
        if (temperature == null) {
            throw new InvalidTemperatureException(null, unit.getSymbol(),
                    "El valor de temperatura no puede ser nulo");
        }

        // Verificar si está por debajo del cero absoluto
        double absoluteZero = (unit == TemperatureUnit.CELSIUS) 
                ? ABSOLUTE_ZERO_CELSIUS 
                : ABSOLUTE_ZERO_FAHRENHEIT;

        if (temperature < absoluteZero) {
            throw InvalidTemperatureException.belowAbsoluteZero(temperature, unit.getSymbol());
        }

        // Verificar si excede el límite máximo razonable
        if (temperature > MAX_REASONABLE_TEMPERATURE) {
            throw InvalidTemperatureException.exceedsMaximum(temperature, unit.getSymbol(), 
                    MAX_REASONABLE_TEMPERATURE);
        }

        // Verificar valores especiales (NaN, Infinity)
        if (temperature.isNaN()) {
            throw new InvalidTemperatureException(temperature, unit.getSymbol(),
                    "El valor de temperatura no puede ser NaN (Not a Number)");
        }

        if (temperature.isInfinite()) {
            throw new InvalidTemperatureException(temperature, unit.getSymbol(),
                    "El valor de temperatura no puede ser infinito");
        }
    }

    /**
     * Verifica si una temperatura está en el punto de congelación del agua.
     *
     * @param celsius temperatura en Celsius
     * @return true si está en el punto de congelación (0°C ± 0.01)
     */
    public boolean isFreezingPoint(Double celsius) {
        if (celsius == null) return false;
        return Math.abs(celsius - 0.0) < 0.01;
    }

    /**
     * Verifica si una temperatura está en el punto de ebullición del agua (a nivel del mar).
     *
     * @param celsius temperatura en Celsius
     * @return true si está en el punto de ebullición (100°C ± 0.01)
     */
    public boolean isBoilingPoint(Double celsius) {
        if (celsius == null) return false;
        return Math.abs(celsius - 100.0) < 0.01;
    }

    /**
     * Obtiene información adicional sobre una temperatura en Celsius.
     *
     * @param celsius temperatura en Celsius
     * @return descripción contextual de la temperatura
     */
    public String getTemperatureContext(Double celsius) {
        if (celsius == null) return "Temperatura no válida";

        if (isFreezingPoint(celsius)) {
            return "Punto de congelación del agua";
        } else if (isBoilingPoint(celsius)) {
            return "Punto de ebullición del agua (a nivel del mar)";
        } else if (Math.abs(celsius - 37.0) < 0.5) {
            return "Temperatura corporal normal";
        } else if (celsius < 0) {
            return "Por debajo del punto de congelación";
        } else if (celsius > 100) {
            return "Por encima del punto de ebullición";
        } else if (celsius >= 20 && celsius <= 25) {
            return "Temperatura ambiente confortable";
        } else {
            return "Temperatura normal";
        }
    }

    /**
     * Obtiene las constantes utilizadas en las conversiones para fines de testing.
     *
     * @return mapa con las constantes del servicio
     */
    public java.util.Map<String, Double> getConversionConstants() {
        java.util.Map<String, Double> constants = new java.util.HashMap<>();
        constants.put("ABSOLUTE_ZERO_CELSIUS", ABSOLUTE_ZERO_CELSIUS);
        constants.put("ABSOLUTE_ZERO_FAHRENHEIT", ABSOLUTE_ZERO_FAHRENHEIT);
        constants.put("MAX_REASONABLE_TEMPERATURE", MAX_REASONABLE_TEMPERATURE);
        return constants;
    }
}
