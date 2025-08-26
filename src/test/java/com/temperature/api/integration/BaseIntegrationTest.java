package com.temperature.api.integration;

import com.temperature.api.TemperatureConverterApplication;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * Clase base para pruebas de integración.
 * 
 * Proporciona configuración común para todas las pruebas de integración,
 * incluyendo la configuración del contexto de Spring Boot y perfiles de test.
 */
@SpringBootTest(
    classes = TemperatureConverterApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@SpringJUnitConfig
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIntegrationTest {

    /**
     * URL base para las pruebas.
     * Se configurará automáticamente con el puerto del servidor de test.
     */
    protected String baseUrl;

    /**
     * Tiempo de espera por defecto para las pruebas (en segundos).
     */
    protected static final int DEFAULT_TIMEOUT = 10;

    /**
     * Método auxiliar para construir URLs de la API.
     *
     * @param endpoint endpoint de la API
     * @return URL completa
     */
    protected String apiUrl(String endpoint) {
        return baseUrl + "/api/temperature" + endpoint;
    }

    /**
     * Método auxiliar para construir URLs de páginas web.
     *
     * @param page página web
     * @return URL completa
     */
    protected String webUrl(String page) {
        return baseUrl + "/" + page;
    }
}
