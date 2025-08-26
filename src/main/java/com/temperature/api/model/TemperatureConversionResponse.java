package com.temperature.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO (Data Transfer Object) para las respuestas de conversión de temperatura.
 * 
 * Esta clase representa los datos de salida de las operaciones de conversión,
 * proporcionando información completa sobre la conversión realizada.
 */
public class TemperatureConversionResponse {

    /**
     * Valor de temperatura original (de entrada).
     */
    @JsonProperty("originalValue")
    private Double originalValue;

    /**
     * Unidad de temperatura original (Celsius o Fahrenheit).
     */
    @JsonProperty("originalUnit")
    private String originalUnit;

    /**
     * Valor de temperatura convertido (resultado).
     */
    @JsonProperty("convertedValue")
    private Double convertedValue;

    /**
     * Unidad de temperatura convertida (Celsius o Fahrenheit).
     */
    @JsonProperty("convertedUnit")
    private String convertedUnit;

    /**
     * Fórmula utilizada para la conversión (para fines educativos).
     */
    @JsonProperty("formula")
    private String formula;

    /**
     * Timestamp de cuando se realizó la conversión.
     */
    @JsonProperty("timestamp")
    private long timestamp;

    /**
     * Constructor por defecto requerido para la serialización JSON.
     */
    public TemperatureConversionResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Constructor completo para crear una respuesta de conversión.
     *
     * @param originalValue   valor original
     * @param originalUnit    unidad original
     * @param convertedValue  valor convertido
     * @param convertedUnit   unidad convertida
     * @param formula         fórmula utilizada
     */
    public TemperatureConversionResponse(Double originalValue, String originalUnit,
                                       Double convertedValue, String convertedUnit,
                                       String formula) {
        this();
        this.originalValue = originalValue;
        this.originalUnit = originalUnit;
        this.convertedValue = convertedValue;
        this.convertedUnit = convertedUnit;
        this.formula = formula;
    }

    // Getters y Setters
    public Double getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(Double originalValue) {
        this.originalValue = originalValue;
    }

    public String getOriginalUnit() {
        return originalUnit;
    }

    public void setOriginalUnit(String originalUnit) {
        this.originalUnit = originalUnit;
    }

    public Double getConvertedValue() {
        return convertedValue;
    }

    public void setConvertedValue(Double convertedValue) {
        this.convertedValue = convertedValue;
    }

    public String getConvertedUnit() {
        return convertedUnit;
    }

    public void setConvertedUnit(String convertedUnit) {
        this.convertedUnit = convertedUnit;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "TemperatureConversionResponse{" +
                "originalValue=" + originalValue +
                ", originalUnit='" + originalUnit + '\'' +
                ", convertedValue=" + convertedValue +
                ", convertedUnit='" + convertedUnit + '\'' +
                ", formula='" + formula + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
