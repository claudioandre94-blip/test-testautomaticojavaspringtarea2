package com.temperature.api.integration;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Duration;

/**
 * Configuración para pruebas de Selenium.
 * 
 * Esta clase proporciona beans de configuración para WebDriver
 * con diferentes navegadores y opciones según el perfil activo.
 */
@Configuration
@Profile("selenium-test")
public class SeleniumTestConfig {

    @Value("${selenium.browser.headless:true}")
    private boolean headless;

    @Value("${selenium.webdriver.timeout:10}")
    private int webDriverTimeout;

    @Value("${selenium.browser.window-size:1920x1080}")
    private String windowSize;

    /**
     * Configura WebDriver para Chrome.
     *
     * @return instancia configurada de ChromeDriver
     */
    @Bean
    public WebDriver chromeWebDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless");
        }

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=" + windowSize);
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");

        // Configurar prefs para deshabilitar notificaciones
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");

        ChromeDriver driver = new ChromeDriver(options);

        // Configurar timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(webDriverTimeout));

        return driver;
    }

    /**
     * Configura WebDriver para Firefox (alternativo).
     *
     * @return instancia configurada de FirefoxDriver
     */
    @Bean("firefoxWebDriver")
    public WebDriver firefoxWebDriver() {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("--headless");
        }

        options.addArguments("--width=1920");
        options.addArguments("--height=1080");

        FirefoxDriver driver = new FirefoxDriver(options);

        // Configurar timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(webDriverTimeout));

        return driver;
    }
}
