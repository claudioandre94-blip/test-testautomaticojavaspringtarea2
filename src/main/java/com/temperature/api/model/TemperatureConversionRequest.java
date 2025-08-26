package com.temperature.api.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * DTO (Data Transfer Object) para las peticiones de conversión de temperatura.
 * 
 * Esta clase representa los datos de entrada para las operaciones de conversión,
 * incluyendo validaciones para asegurar que los valores estén dentro de rangos físicos válidos.
 */
public class TemperatureConversionRequest {

    /**
     * Valor de temperatura a convertir.
     * Los límites están basados en temperaturas físicamente alcanzables:
     * - Mínimo: Cero absoluto en Celsius (-273.15°C)
     * - Máximo: Temperatura razonable para aplicaciones prácticas (10,000°)
     */
    @NotNull(message = "El valor de temperatura es requerido")
    @DecimalMin(value = "-273.15", message = "La temperatura no puede ser menor al cero absoluto (-273.15°C)")
    @DecimalMax(value = "10000.0", message = "La temperatura no puede exceder los 10,000 grados")
    private Double value;

    /**
     * Constructor por defecto requerido para la deserialización JSON.
     */
    public TemperatureConversionRequest() {
    }

    /**
     * Constructor con parámetros para crear una petición de conversión.
     *
     * @param value valor de temperatura a convertir
     */
    public TemperatureConversionRequest(Double value) {
        this.value = value;
    }

    /**
     * Obtiene el valor de temperatura.
     *
     * @return valor de temperatura
     */
    public Double getValue() {
        return value;
    }

    /**
     * Establece el valor de temperatura.
     *
     * @param value nuevo valor de temperatura
     */
    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TemperatureConversionRequest{" +
                "value=" + value +
                '}';
    }
}
