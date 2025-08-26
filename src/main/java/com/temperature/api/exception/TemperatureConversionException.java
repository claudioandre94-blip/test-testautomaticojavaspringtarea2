package com.temperature.api.exception;

/**
 * Excepción personalizada para errores relacionados con conversiones de temperatura.
 * 
 * Esta excepción se lanza cuando ocurren errores específicos durante el proceso
 * de conversión de temperaturas, como valores fuera de rango o unidades inválidas.
 */
public class TemperatureConversionException extends RuntimeException {

    private final String errorCode;
    private final Object[] errorArgs;

    /**
     * Constructor con mensaje de error.
     *
     * @param message mensaje descriptivo del error
     */
    public TemperatureConversionException(String message) {
        super(message);
        this.errorCode = "TEMPERATURE_CONVERSION_ERROR";
        this.errorArgs = new Object[0];
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param message mensaje descriptivo del error
     * @param cause   causa raíz del error
     */
    public TemperatureConversionException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "TEMPERATURE_CONVERSION_ERROR";
        this.errorArgs = new Object[0];
    }

    /**
     * Constructor completo con código de error y argumentos.
     *
     * @param message   mensaje descriptivo del error
     * @param errorCode código específico del error
     * @param errorArgs argumentos adicionales para el error
     */
    public TemperatureConversionException(String message, String errorCode, Object... errorArgs) {
        super(message);
        this.errorCode = errorCode;
        this.errorArgs = errorArgs != null ? errorArgs : new Object[0];
    }

    /**
     * Obtiene el código específico del error.
     *
     * @return código del error
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Obtiene los argumentos adicionales del error.
     *
     * @return array de argumentos
     */
    public Object[] getErrorArgs() {
        return errorArgs;
    }
}
