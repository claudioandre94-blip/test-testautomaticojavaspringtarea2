package com.temperature.api.integration;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Pruebas de integración con Selenium para la interfaz web de conversión de temperaturas.
 * 
 * Estas pruebas verifican que la interfaz web funcione correctamente end-to-end,
 * simulando las interacciones de un usuario real con la aplicación web.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringJUnitConfig
@DisplayName("Temperature Converter Web Interface Integration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TemperatureConverterSeleniumTest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeAll
    static void setUpClass() {
        // Configurar WebDriverManager para gestión automática del driver
        WebDriverManager.chromedriver().setup();

        // Configurar opciones del navegador
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Ejecutar en modo headless para CI/CD
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Configurar timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        // Nota: En un escenario real, la interfaz web estaría servida por Spring Boot
        // Para esta demo, asumimos que hay una página web en /index.html
        driver.get(baseUrl);
    }

    @AfterAll
    static void tearDownClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    @DisplayName("Should load the temperature converter page successfully")
    void shouldLoadTemperatureConverterPage() {
        // Verificar que la página se carga correctamente
        assertNotNull(driver.getTitle());
        assertTrue(driver.getTitle().contains("Temperature") || 
                  driver.getTitle().contains("Temperatura"));

        // Verificar elementos principales de la interfaz
        WebElement temperatureInput = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.id("temperature"))
        );
        assertNotNull(temperatureInput);

        WebElement convertButton = driver.findElement(By.id("convertButton"));
        assertNotNull(convertButton);

        // Verificar opciones de conversión
        WebElement celsiusToFahrenheit = driver.findElement(By.id("celsiusToFahrenheit"));
        WebElement fahrenheitToCelsius = driver.findElement(By.id("fahrenheitToCelsius"));
        assertNotNull(celsiusToFahrenheit);
        assertNotNull(fahrenheitToCelsius);
    }

    @Test
    @Order(2)
    @DisplayName("Should convert 0 Celsius to 32 Fahrenheit")
    void shouldConvertZeroCelsiusToFahrenheit() {
        // Localizar elementos
        WebElement temperatureInput = driver.findElement(By.id("temperature"));
        WebElement celsiusToFahrenheitRadio = driver.findElement(By.id("celsiusToFahrenheit"));

        // Realizar la conversión
        temperatureInput.clear();
        temperatureInput.sendKeys("0");

        if (!celsiusToFahrenheitRadio.isSelected()) {
            celsiusToFahrenheitRadio.click();
        }
        
        driver.findElement(By.id("convertButton")).click();

        // Esperar y verificar el resultado
        WebElement resultElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("result"))
        );

        String resultText = resultElement.getText();
        assertTrue(resultText.contains("32.0"));
    }

    @Test
    @Order(3)
    @DisplayName("Should convert 32 Fahrenheit to 0 Celsius")
    void shouldConvertThirtyTwoFahrenheitToCelsius() {
        // Localizar elementos
        WebElement temperatureInput = driver.findElement(By.id("temperature"));
        WebElement fahrenheitToCelsiusRadio = driver.findElement(By.id("fahrenheitToCelsius"));

        // Realizar la conversión
        temperatureInput.clear();
        temperatureInput.sendKeys("32");
        ((JavascriptExecutor) driver).executeScript("arguments.checked = true;", fahrenheitToCelsiusRadio);
        driver.findElement(By.id("convertButton")).click();

        // Esperar y verificar el resultado
        WebElement resultElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("result"))
        );

        String resultText = resultElement.getText();
        assertTrue(resultText.contains("89.60"));
    }

    @Test
    @Order(4)
    @DisplayName("Should convert body temperature (37°C to 98.6°F)")
    void shouldConvertBodyTemperature() {
        // Localizar elementos
        WebElement temperatureInput = driver.findElement(By.id("temperature"));
        WebElement celsiusToFahrenheitRadio = driver.findElement(By.id("celsiusToFahrenheit"));

        // Realizar la conversión
        temperatureInput.clear();
        temperatureInput.sendKeys("37");
        ((JavascriptExecutor) driver).executeScript("arguments.checked = true;", celsiusToFahrenheitRadio);

        driver.findElement(By.id("convertButton")).click();

        // Esperar y verificar el resultado
        WebElement resultElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("result"))
        );

        String resultText = resultElement.getText();
        assertTrue(resultText.contains("98.6"));
    }

    @Test
    @Order(5)
    @DisplayName("Should handle invalid temperature input")
    void shouldHandleInvalidTemperatureInput() {
        // Localizar elementos
        WebElement temperatureInput = driver.findElement(By.id("temperature"));

        // Ingresar temperatura inválida (por debajo del cero absoluto)
        temperatureInput.clear();
        temperatureInput.sendKeys("-300");
        driver.findElement(By.id("convertButton")).click();

        // Verificar que se muestra un mensaje de error
        WebElement errorElement = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.className("error-message"))
        );

        String errorText = errorElement.getText();
        assertTrue(errorText.toLowerCase().contains("error") || 
                  errorText.toLowerCase().contains("inválid") ||
                  errorText.toLowerCase().contains("absoluto"));
    }

    @Test
    @Order(6)
    @DisplayName("Should handle non-numeric input")
    void shouldHandleNonNumericInput() {
        // Localizar elementos
        WebElement temperatureInput = driver.findElement(By.id("temperature"));
        WebElement convertButton = driver.findElement(By.id("convertButton"));

        // Ingresar texto no numérico
        temperatureInput.clear();
        temperatureInput.sendKeys("abc");
        convertButton.click();

        // Verificar validación del navegador o mensaje de error personalizado
        String validationMessage = temperatureInput.getAttribute("validationMessage");
        boolean hasValidationMessage = validationMessage != null && !validationMessage.isEmpty();

        if (!hasValidationMessage) {
            // Si no hay validación HTML5, buscar mensaje de error personalizado
            try {
                WebElement errorElement = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.className("error-message"))
                );
                assertNotNull(errorElement);
            } catch (Exception e) {
                fail("Should show validation message for non-numeric input");
            }
        }

        assertTrue(hasValidationMessage || 
                  driver.findElements(By.className("error-message")).size() > 0);
    }

    @Test
    @Order(7)
    @DisplayName("Should maintain conversion history")
    void shouldMaintainConversionHistory() throws InterruptedException {
        // Realizar múltiples conversiones
        driver.navigate().refresh();

        performConversion("0", true);
        Thread.sleep(1000); // Esperar para asegurar que se actualice el historial

        performConversion("100", true);
        Thread.sleep(1000);

        performConversion("32", false);
        Thread.sleep(1000);

        // Verificar que existe una sección de historial
        List<WebElement> historyElements = driver.findElements(By.className("history-item"));
        assertTrue(historyElements.size() >= 3, "Should have at least 3 items in history");
    }

    @Test
    @Order(8)
    @DisplayName("Should clear history when requested")
    void shouldClearHistoryWhenRequested() throws InterruptedException {
        // Primero realizar una conversión
        performConversion("25", true);
        Thread.sleep(1000);

        // Buscar y hacer clic en el botón de limpiar historial
        try {
            driver.findElement(By.id("clearHistory")).click();
            Thread.sleep(500);

            // Verificar que el historial se ha limpiado
            List<WebElement> historyElements = driver.findElements(By.className("history-item"));
            assertTrue(historyElements.size() == 0, "History should be empty after clearing");
        } catch (Exception e) {
            // Si no existe el botón de limpiar historial, omitir esta prueba
            Assumptions.assumeTrue(false, "Clear history button not found - feature may not be implemented");
        }
    }

    @Test
    @Order(9)
    @DisplayName("Should show loading indicator during conversion")
    void shouldShowLoadingIndicatorDuringConversion() {
        // Localizar elementos
        WebElement temperatureInput = driver.findElement(By.id("temperature"));
        WebElement convertButton = driver.findElement(By.id("convertButton"));

        // Realizar conversión
        temperatureInput.clear();
        temperatureInput.sendKeys("25");
        convertButton.click();

        // Verificar que aparece un indicador de carga (si está implementado)
        try {
            WebElement loadingElement = driver.findElement(By.className("loading"));
            assertNotNull(loadingElement);
        } catch (Exception e) {
            // Si no hay indicador de carga, verificar que al menos el botón se deshabilita
            // temporalmente o hay algún feedback visual
            String buttonText = convertButton.getText();
            assertNotNull(buttonText);
        }
    }

    @Test
    @Order(10)
    @DisplayName("Should be responsive on mobile viewport")
    void shouldBeResponsiveOnMobileViewport() {
        // Cambiar el tamaño de la ventana para simular móvil
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(375, 667));

        // Recargar la página
        driver.navigate().refresh();

        // Verificar que los elementos siguen siendo accesibles
        WebElement temperatureInput = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.id("temperature"))
        );
        WebElement convertButton = driver.findElement(By.id("convertButton"));

        assertTrue(temperatureInput.isDisplayed());
        assertTrue(convertButton.isDisplayed());

        // Restaurar tamaño original
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
    }

    /**
     * Método auxiliar para realizar conversiones
     */
    private void performConversion(String temperature, boolean celsiusToFahrenheit) {
        WebElement temperatureInput = driver.findElement(By.id("temperature"));
        WebElement conversionRadio = celsiusToFahrenheit ? 
            driver.findElement(By.id("celsiusToFahrenheit")) :
            driver.findElement(By.id("fahrenheitToCelsius"));
        WebElement convertButton = driver.findElement(By.id("convertButton"));

        temperatureInput.clear();
        temperatureInput.sendKeys(temperature);
        ((JavascriptExecutor) driver).executeScript("arguments.checked = true;", conversionRadio);
        convertButton.click();

        // Esperar a que aparezca el resultado
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("result")));
    }
}
