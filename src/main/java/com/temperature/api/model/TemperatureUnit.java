package com.temperature.api.model;

/**
 * Enumeración que representa las unidades de temperatura soportadas por la API.
 * 
 * Esta enumeración proporciona las unidades de temperatura disponibles
 * y métodos utilitarios para trabajar con ellas.
 */
public enum TemperatureUnit {

    /**
     * Grados Celsius (°C) - Unidad del Sistema Internacional.
     */
    CELSIUS("°C", "Celsius"),

    /**
     * Grados Fahrenheit (°F) - Unidad del sistema imperial.
     */
    FAHRENHEIT("°F", "Fahrenheit");

    private final String symbol;
    private final String displayName;

    /**
     * Constructor del enum.
     *
     * @param symbol      símbolo de la unidad (ej: °C, °F)
     * @param displayName nombre completo para mostrar
     */
    TemperatureUnit(String symbol, String displayName) {
        this.symbol = symbol;
        this.displayName = displayName;
    }

    /**
     * Obtiene el símbolo de la unidad de temperatura.
     *
     * @return símbolo de la unidad
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Obtiene el nombre completo de la unidad de temperatura.
     *
     * @return nombre para mostrar
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Convierte una cadena de texto a la unidad de temperatura correspondiente.
     *
     * @param unit cadena con el nombre de la unidad
     * @return la unidad de temperatura correspondiente
     * @throws IllegalArgumentException si la unidad no es válida
     */
    public static TemperatureUnit fromString(String unit) {
        if (unit == null || unit.trim().isEmpty()) {
            throw new IllegalArgumentException("La unidad de temperatura no puede estar vacía");
        }

        String upperUnit = unit.trim().toUpperCase();

        switch (upperUnit) {
            case "CELSIUS":
            case "C":
            case "°C":
                return CELSIUS;
            case "FAHRENHEIT":
            case "F":
            case "°F":
                return FAHRENHEIT;
            default:
                throw new IllegalArgumentException("Unidad de temperatura no válida: " + unit + 
                    ". Las unidades válidas son: Celsius, C, °C, Fahrenheit, F, °F");
        }
    }

    @Override
    public String toString() {
        return displayName + " (" + symbol + ")";
    }
}
