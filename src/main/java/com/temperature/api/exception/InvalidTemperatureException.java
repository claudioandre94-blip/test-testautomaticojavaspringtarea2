package com.temperature.api.exception;

/**
 * Excepción específica para temperaturas que no son válidas físicamente.
 * 
 * Esta excepción se lanza cuando se intenta convertir una temperatura que está
 * por debajo del cero absoluto o excede los límites razonables de la aplicación.
 */
public class InvalidTemperatureException extends TemperatureConversionException {

    private final Double invalidValue;
    private final String unit;

    /**
     * Constructor para temperatura inválida.
     *
     * @param invalidValue valor de temperatura inválido
     * @param unit         unidad de la temperatura
     * @param message      mensaje descriptivo del error
     */
    public InvalidTemperatureException(Double invalidValue, String unit, String message) {
        super(message, "INVALID_TEMPERATURE_VALUE", invalidValue, unit);
        this.invalidValue = invalidValue;
        this.unit = unit;
    }

    /**
     * Crea una excepción para temperatura por debajo del cero absoluto.
     *
     * @param value valor de temperatura inválido
     * @param unit  unidad de la temperatura
     * @return nueva instancia de la excepción
     */
    public static InvalidTemperatureException belowAbsoluteZero(Double value, String unit) {
        String message = String.format("La temperatura %.2f%s está por debajo del cero absoluto. " +
                "El cero absoluto es -273.15°C (-459.67°F).", value, unit);
        return new InvalidTemperatureException(value, unit, message);
    }

    /**
     * Crea una excepción para temperatura que excede el límite máximo.
     *
     * @param value    valor de temperatura inválido
     * @param unit     unidad de la temperatura
     * @param maxLimit límite máximo permitido
     * @return nueva instancia de la excepción
     */
    public static InvalidTemperatureException exceedsMaximum(Double value, String unit, Double maxLimit) {
        String message = String.format("La temperatura %.2f%s excede el límite máximo de %.1f grados.",
                value, unit, maxLimit);
        return new InvalidTemperatureException(value, unit, message);
    }

    /**
     * Obtiene el valor de temperatura inválido.
     *
     * @return valor inválido
     */
    public Double getInvalidValue() {
        return invalidValue;
    }

    /**
     * Obtiene la unidad de la temperatura inválida.
     *
     * @return unidad de temperatura
     */
    public String getUnit() {
        return unit;
    }
}
