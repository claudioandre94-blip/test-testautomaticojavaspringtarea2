package com.temperature.api.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Configuración para la documentación OpenAPI/Swagger de la API.
 * 
 * Esta configuración personaliza la documentación automática de la API,
 * proporcionando información detallada sobre los endpoints, esquemas y ejemplos.
 */
@Configuration
public class OpenApiConfig {

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${app.title:Temperature Conversion API}")
    private String appTitle;

    @Value("${app.description:API REST en Java para conversión de temperaturas}")
    private String appDescription;

    /**
     * Configuración personalizada de OpenAPI.
     *
     * @return instancia configurada de OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(appTitle)
                        .version(appVersion)
                        .description(appDescription + "\n\n" +
                                "Esta API permite convertir temperaturas entre las escalas Celsius y Fahrenheit " +
                                "utilizando las fórmulas matemáticas estándar. Incluye validaciones para asegurar " +
                                "que las temperaturas estén dentro de rangos físicos válidos.")
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de desarrollo")
                ));
    }
}
