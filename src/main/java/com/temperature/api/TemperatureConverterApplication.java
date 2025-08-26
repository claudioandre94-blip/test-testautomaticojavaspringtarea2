package com.temperature.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación Spring Boot para conversión de temperaturas entre Celsius y Fahrenheit.
 * 
 * Esta aplicación proporciona una API REST con endpoints para:
 * - Convertir de Celsius a Fahrenheit
 * - Convertir de Fahrenheit a Celsius
 * 
 * Incluye una interfaz web sencilla para interactuar con la API.
 * 
 * @author Sistema de Conversión de Temperaturas
 * @version 1.0.0
 */
@SpringBootApplication
public class TemperatureConverterApplication {

    /**
     * Método principal que inicia la aplicación Spring Boot.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(TemperatureConverterApplication.class, args);
    }
}
