# 🌡️ Temperature Converter API

Una **API REST completa** desarrollada en **Java Spring Boot** para conversión de temperaturas entre Celsius y Fahrenheit, que incluye una interfaz web moderna y un conjunto completo de pruebas unitarias y de integración con Selenium.

## 📋 Características Principales

### 🔧 Tecnologías Utilizadas
- **Java 17**
- **Spring Boot 3.2.0**
- **Maven** para gestión de dependencias
- **JUnit 5** para pruebas unitarias
- **Mockito** para mocking en pruebas
- **Selenium WebDriver 4.15.0** para pruebas de integración
- **WebDriverManager** para gestión automática de drivers
- **Swagger/OpenAPI** para documentación de la API
- **HTML5, CSS3, JavaScript** para la interfaz web

### 🚀 Funcionalidades de la API
- ✅ Conversión de Celsius a Fahrenheit
- ✅ Conversión de Fahrenheit a Celsius
- ✅ Validación de temperaturas físicamente válidas
- ✅ Manejo de errores personalizado
- ✅ Documentación automática con Swagger
- ✅ Endpoints de monitoreo y salud
- ✅ Soporte para CORS

### 🌐 Interfaz Web
- ✅ Diseño responsive y moderno
- ✅ Validación en tiempo real
- ✅ Historial de conversiones
- ✅ Información contextual sobre temperaturas
- ✅ Indicadores de carga y feedback visual

### 🧪 Testing Completo
- ✅ **Pruebas Unitarias** con JUnit 5 y Mockito
- ✅ **Pruebas de Integración** con MockMvc
- ✅ **Pruebas End-to-End** con Selenium WebDriver
- ✅ Cobertura de código con JaCoCo

## 📁 Estructura del Proyecto

```
temperature-converter-api/
├── src/
│   ├── main/
│   │   ├── java/com/temperature/api/
│   │   │   ├── TemperatureConverterApplication.java    # Clase principal
│   │   │   ├── controller/
│   │   │   │   └── TemperatureController.java          # Controlador REST
│   │   │   ├── service/
│   │   │   │   └── TemperatureConversionService.java   # Lógica de negocio
│   │   │   ├── model/
│   │   │   │   ├── TemperatureConversionRequest.java   # DTO de petición
│   │   │   │   ├── TemperatureConversionResponse.java  # DTO de respuesta
│   │   │   │   └── TemperatureUnit.java               # Enum de unidades
│   │   │   ├── exception/
│   │   │   │   ├── TemperatureConversionException.java # Excepciones personalizadas
│   │   │   │   ├── InvalidTemperatureException.java
│   │   │   │   └── GlobalExceptionHandler.java        # Manejo global de errores
│   │   │   └── config/
│   │   │       ├── OpenApiConfig.java                 # Configuración Swagger
│   │   │       └── CorsConfig.java                    # Configuración CORS
│   │   └── resources/
│   │       ├── static/                               # Interfaz web
│   │       │   ├── index.html
│   │       │   ├── styles.css
│   │       │   └── script.js
│   │       └── application.yml                       # Configuración principal
│   └── test/
│       ├── java/com/temperature/api/
│       │   ├── controller/
│       │   │   └── TemperatureControllerTest.java     # Tests del controlador
│       │   ├── service/
│       │   │   └── TemperatureConversionServiceTest.java # Tests del servicio
│       │   └── integration/
│       │       ├── TemperatureConverterSeleniumTest.java # Tests Selenium
│       │       ├── SeleniumTestConfig.java
│       │       └── BaseIntegrationTest.java
│       └── resources/
│           └── application-test.yml                  # Configuración de tests
├── pom.xml                                          # Configuración Maven
└── README.md
```

## 🔗 Endpoints de la API

### Conversión de Temperaturas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/temperature/celsius-to-fahrenheit/{value}` | Convierte Celsius a Fahrenheit |
| `GET` | `/api/temperature/fahrenheit-to-celsius/{value}` | Convierte Fahrenheit a Celsius |
| `POST` | `/api/temperature/celsius-to-fahrenheit` | Convierte Celsius a Fahrenheit (JSON) |
| `POST` | `/api/temperature/fahrenheit-to-celsius` | Convierte Fahrenheit a Celsius (JSON) |

### Monitoreo y Utilidades

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/temperature/health` | Estado de salud de la API |
| `GET` | `/api/temperature/info` | Información de la API y constantes |

### Documentación

| Endpoint | Descripción |
|----------|-------------|
| `/api/swagger-ui.html` | Interfaz de Swagger UI |
| `/api/docs` | Especificación OpenAPI JSON |

## 🚀 Instalación y Ejecución

### Prerrequisitos
- **Java 17** o superior
- **Maven 3.6** o superior
- **Git** (opcional)

### 1. Clonar el Proyecto
```bash
git clone <repository-url>
cd temperature-converter-api
```

### 2. Compilar el Proyecto
```bash
mvn clean compile
```

### 3. Ejecutar Pruebas Unitarias
```bash
mvn test
```

### 4. Ejecutar Pruebas de Integración
```bash
mvn verify
```

### 5. Ejecutar solo Pruebas de Selenium
```bash
mvn test -P selenium-tests
```

### 6. Ejecutar la Aplicación
```bash
mvn spring-boot:run
```

### 7. Generar Reporte de Cobertura
```bash
mvn clean test jacoco:report
# El reporte se genera en target/site/jacoco/index.html
```

## 📱 Acceso a la Aplicación

Una vez ejecutada la aplicación, estará disponible en:

- **Interfaz Web**: http://localhost:8080
- **API REST**: http://localhost:8080/api/temperature
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **Health Check**: http://localhost:8080/api/temperature/health

## 🧪 Ejemplos de Uso de la API

### Ejemplo 1: Convertir 25°C a Fahrenheit (GET)
```bash
curl -X GET "http://localhost:8080/api/temperature/celsius-to-fahrenheit/25"
```

**Respuesta:**
```json
{
  "originalValue": 25.0,
  "originalUnit": "Celsius",
  "convertedValue": 77.0,
  "convertedUnit": "Fahrenheit",
  "formula": "F = (C × 9/5) + 32",
  "timestamp": 1703123456789
}
```

### Ejemplo 2: Convertir 77°F a Celsius (POST)
```bash
curl -X POST "http://localhost:8080/api/temperature/fahrenheit-to-celsius" \
  -H "Content-Type: application/json" \
  -d '{"value": 77.0}'
```

**Respuesta:**
```json
{
  "originalValue": 77.0,
  "originalUnit": "Fahrenheit",
  "convertedValue": 25.0,
  "convertedUnit": "Celsius",
  "formula": "C = (F - 32) × 5/9",
  "timestamp": 1703123456789
}
```

### Ejemplo 3: Verificar Estado de la API
```bash
curl -X GET "http://localhost:8080/api/temperature/health"
```

**Respuesta:**
```json
{
  "status": "UP",
  "service": "Temperature Conversion API",
  "version": "1.0.0",
  "serviceCheck": "OK",
  "timestamp": 1703123456789
}
```

## 🔬 Fórmulas Utilizadas

### Celsius a Fahrenheit
```
F = (C × 9/5) + 32
```

### Fahrenheit a Celsius
```
C = (F - 32) × 5/9
```

## ⚠️ Validaciones

La API incluye las siguientes validaciones:

- ❌ **Cero Absoluto**: No permite temperaturas por debajo del cero absoluto (-273.15°C / -459.67°F)
- ❌ **Límite Máximo**: No permite temperaturas superiores a 10,000 grados
- ❌ **Valores Especiales**: Rechaza `NaN` e `Infinity`
- ❌ **Valores Nulos**: Rechaza valores nulos o vacíos

## 🧪 Ejecutar Tipos Específicos de Pruebas

### Solo Pruebas Unitarias
```bash
mvn test -Dtest="*Test"
```

### Solo Pruebas de Integración
```bash
mvn test -Dtest="*IT,*IntegrationTest"
```

### Solo Pruebas de Selenium
```bash
mvn test -Dtest="*SeleniumTest"
```

### Todas las Pruebas con Reporte
```bash
mvn clean verify jacoco:report
```

## 📊 Cobertura de Código

El proyecto incluye configuración de JaCoCo para generar reportes de cobertura:

```bash
mvn clean test jacoco:report
open target/site/jacoco/index.html  # macOS
start target/site/jacoco/index.html # Windows
```

## 🐳 Configuraciones Adicionales

### Perfiles Maven Disponibles

- **default**: Ejecuta pruebas unitarias
- **integration-tests**: Ejecuta pruebas de integración
- **selenium-tests**: Ejecuta solo pruebas de Selenium

### Variables de Entorno para Testing

```bash
# Para pruebas de Selenium
export SELENIUM_BROWSER_HEADLESS=true
export SELENIUM_WEBDRIVER_TIMEOUT=10
export SELENIUM_BROWSER_WINDOW_SIZE=1920x1080
```

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu característica (`git checkout -b feature/nueva-caracteristica`)
3. Commit tus cambios (`git commit -am 'Agrega nueva característica'`)
4. Push a la rama (`git push origin feature/nueva-caracteristica`)
5. Crea un Pull Request

## 📝 Notas Técnicas

### Precisión Decimal
- Las conversiones utilizan `BigDecimal` para máxima precisión
- Los resultados se redondean a 2 decimales

### Manejo de Errores
- Excepciones personalizadas para diferentes tipos de errores
- Respuestas HTTP estructuradas con códigos de estado apropiados
- Logging detallado para debugging

### Seguridad
- CORS configurado para desarrollo (ajustar para producción)
- Validación de entrada en múltiples capas
- No exposición de stacktraces en producción

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👨‍💻 Autor

Desarrollado como ejemplo de API REST completa con Spring Boot, incluyendo testing exhaustivo y interfaz web moderna.

---

**¿Encontraste algún problema o tienes alguna sugerencia?** 
¡Abre un issue o envía un pull request! 🚀
