/**
 * Aplicación de Conversión de Temperaturas
 * Interactúa con la API REST Spring Boot para realizar conversiones
 */

// Configuración de la aplicación
const CONFIG = {
    API_BASE_URL: '/api/temperature',
    MAX_HISTORY_ITEMS: 10,
    ANIMATION_DURATION: 300
};

// Estado global de la aplicación
let conversionHistory = [];

// Referencias a elementos del DOM
let elements = {};

/**
 * Inicialización de la aplicación
 */
document.addEventListener('DOMContentLoaded', function() {
    initializeElements();
    attachEventListeners();
    loadHistoryFromStorage();
    checkApiHealth();
});

/**
 * Inicializa las referencias a elementos del DOM
 */
function initializeElements() {
    elements = {
        form: document.getElementById('temperatureForm'),
        temperatureInput: document.getElementById('temperature'),
        convertButton: document.getElementById('convertButton'),
        btnText: document.querySelector('.btn-text'),
        loadingSpinner: document.querySelector('.loading-spinner'),
        result: document.getElementById('result'),
        errorResult: document.getElementById('errorResult'),
        originalTemp: document.getElementById('originalTemp'),
        originalUnit: document.getElementById('originalUnit'),
        convertedTemp: document.getElementById('convertedTemp'),
        convertedUnit: document.getElementById('convertedUnit'),
        formulaUsed: document.getElementById('formulaUsed'),
        temperatureContext: document.getElementById('temperatureContext'),
        errorMessage: document.getElementById('errorMessage'),
        historyContainer: document.getElementById('historyContainer'),
        clearHistoryBtn: document.getElementById('clearHistory'),
        inputError: document.getElementById('inputError')
    };
}

/**
 * Adjunta event listeners a los elementos
 */
function attachEventListeners() {
    elements.form.addEventListener('submit', handleFormSubmit);
    elements.clearHistoryBtn.addEventListener('click', clearHistory);
    elements.temperatureInput.addEventListener('input', validateInput);
    elements.temperatureInput.addEventListener('blur', validateInput);
}

/**
 * Maneja el envío del formulario
 */
async function handleFormSubmit(event) {
    event.preventDefault();

    const temperature = parseFloat(elements.temperatureInput.value);
    const conversionType = document.querySelector('input[name="conversionType"]:checked').value;

    if (!validateTemperature(temperature)) {
        return;
    }

    try {
        showLoading(true);
        hideResults();

        const result = await performConversion(temperature, conversionType);
        displayResult(result);
        addToHistory(result);

    } catch (error) {
        displayError(error.message);
    } finally {
        showLoading(false);
    }
}

/**
 * Realiza la conversión llamando a la API
 */
async function performConversion(temperature, conversionType) {
    const endpoint = `${CONFIG.API_BASE_URL}/${conversionType}/${temperature}`;

    const response = await fetch(endpoint, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || `Error ${response.status}: ${response.statusText}`);
    }

    return await response.json();
}

/**
 * Valida la entrada de temperatura
 */
function validateTemperature(temperature) {
    clearInputError();

    if (isNaN(temperature)) {
        showInputError('Por favor ingrese un número válido');
        return false;
    }

    if (temperature < -273.15) {
        showInputError('La temperatura no puede estar por debajo del cero absoluto (-273.15°C)');
        return false;
    }

    if (temperature > 10000) {
        showInputError('La temperatura no puede exceder los 10,000 grados');
        return false;
    }

    return true;
}

/**
 * Valida la entrada en tiempo real
 */
function validateInput() {
    const value = elements.temperatureInput.value;
    if (value && value.trim() !== '') {
        validateTemperature(parseFloat(value));
    } else {
        clearInputError();
    }
}

/**
 * Muestra error de entrada
 */
function showInputError(message) {
    elements.inputError.textContent = message;
    elements.inputError.classList.remove('hidden');
    elements.temperatureInput.classList.add('error');
}

/**
 * Limpia error de entrada
 */
function clearInputError() {
    elements.inputError.classList.add('hidden');
    elements.temperatureInput.classList.remove('error');
}

/**
 * Muestra/oculta el indicador de carga
 */
function showLoading(show) {
    if (show) {
        elements.btnText.classList.add('hidden');
        elements.loadingSpinner.classList.remove('hidden');
        elements.convertButton.disabled = true;
    } else {
        elements.btnText.classList.remove('hidden');
        elements.loadingSpinner.classList.add('hidden');
        elements.convertButton.disabled = false;
    }
}

/**
 * Muestra el resultado de la conversión
 */
function displayResult(data) {
    elements.originalTemp.textContent = data.originalValue.toFixed(2);
    elements.originalUnit.textContent = getUnitSymbol(data.originalUnit);
    elements.convertedTemp.textContent = data.convertedValue.toFixed(2);
    elements.convertedUnit.textContent = getUnitSymbol(data.convertedUnit);
    elements.formulaUsed.textContent = data.formula;

    // Obtener contexto de temperatura
    const context = getTemperatureContext(data.convertedValue, data.convertedUnit);
    elements.temperatureContext.textContent = context;

    elements.result.classList.remove('hidden');
    elements.errorResult.classList.add('hidden');

    // Animación suave
    elements.result.style.opacity = '0';
    setTimeout(() => {
        elements.result.style.opacity = '1';
    }, 50);
}

/**
 * Muestra error de conversión
 */
function displayError(message) {
    elements.errorMessage.textContent = message;
    elements.errorResult.classList.remove('hidden');
    elements.result.classList.add('hidden');
}

/**
 * Oculta los resultados
 */
function hideResults() {
    elements.result.classList.add('hidden');
    elements.errorResult.classList.add('hidden');
}

/**
 * Obtiene el símbolo de la unidad
 */
function getUnitSymbol(unit) {
    switch (unit.toLowerCase()) {
        case 'celsius':
            return '°C';
        case 'fahrenheit':
            return '°F';
        default:
            return unit;
    }
}

/**
 * Obtiene contexto sobre la temperatura
 */
function getTemperatureContext(value, unit) {
    let celsius = value;

    // Convertir a Celsius si es necesario
    if (unit.toLowerCase() === 'fahrenheit') {
        celsius = (value - 32) * 5 / 9;
    }

    if (Math.abs(celsius) < 0.01) {
        return '❄️ Punto de congelación del agua';
    } else if (Math.abs(celsius - 100) < 0.01) {
        return '💨 Punto de ebullición del agua';
    } else if (Math.abs(celsius - 37) < 0.5) {
        return '🌡️ Temperatura corporal normal';
    } else if (celsius < 0) {
        return '🧊 Por debajo del punto de congelación';
    } else if (celsius > 100) {
        return '🔥 Por encima del punto de ebullición';
    } else if (celsius >= 20 && celsius <= 25) {
        return '🏠 Temperatura ambiente confortable';
    } else if (celsius < -100) {
        return '🌨️ Extremadamente frío';
    } else if (celsius > 50) {
        return '☀️ Extremadamente caliente';
    } else {
        return '📏 Temperatura normal';
    }
}

/**
 * Añade conversión al historial
 */
function addToHistory(data) {
    const historyItem = {
        id: Date.now(),
        originalValue: data.originalValue,
        originalUnit: data.originalUnit,
        convertedValue: data.convertedValue,
        convertedUnit: data.convertedUnit,
        formula: data.formula,
        timestamp: new Date().toLocaleString()
    };

    conversionHistory.unshift(historyItem);

    // Mantener solo los últimos N elementos
    if (conversionHistory.length > CONFIG.MAX_HISTORY_ITEMS) {
        conversionHistory = conversionHistory.slice(0, CONFIG.MAX_HISTORY_ITEMS);
    }

    updateHistoryDisplay();
    saveHistoryToStorage();
}

/**
 * Actualiza la visualización del historial
 */
function updateHistoryDisplay() {
    if (conversionHistory.length === 0) {
        elements.historyContainer.innerHTML = '<div class="history-empty"><p>No hay conversiones realizadas aún</p></div>';
        return;
    }

    const historyHTML = conversionHistory.map(item => `
        <div class="history-item" data-id="${item.id}">
            <div class="history-conversion">
                <span class="history-original">${item.originalValue.toFixed(2)} ${getUnitSymbol(item.originalUnit)}</span>
                <span class="history-arrow">→</span>
                <span class="history-converted">${item.convertedValue.toFixed(2)} ${getUnitSymbol(item.convertedUnit)}</span>
            </div>
            <div class="history-meta">
                <small class="history-time">${item.timestamp}</small>
                <button class="history-remove" onclick="removeHistoryItem(${item.id})" title="Eliminar">❌</button>
            </div>
        </div>
    `).join('');

    elements.historyContainer.innerHTML = historyHTML;
}

/**
 * Elimina un elemento del historial
 */
function removeHistoryItem(id) {
    conversionHistory = conversionHistory.filter(item => item.id !== id);
    updateHistoryDisplay();
    saveHistoryToStorage();
}

/**
 * Limpia todo el historial
 */
function clearHistory() {
    if (confirm('¿Está seguro de que desea limpiar todo el historial?')) {
        conversionHistory = [];
        updateHistoryDisplay();
        saveHistoryToStorage();
    }
}

/**
 * Guarda el historial en localStorage
 */
function saveHistoryToStorage() {
    try {
        localStorage.setItem('temperatureHistory', JSON.stringify(conversionHistory));
    } catch (error) {
        console.warn('No se pudo guardar el historial:', error);
    }
}

/**
 * Carga el historial desde localStorage
 */
function loadHistoryFromStorage() {
    try {
        const saved = localStorage.getItem('temperatureHistory');
        if (saved) {
            conversionHistory = JSON.parse(saved);
            updateHistoryDisplay();
        }
    } catch (error) {
        console.warn('No se pudo cargar el historial:', error);
    }
}

/**
 * Verifica la salud de la API
 */
async function checkApiHealth() {
    try {
        const response = await fetch(`${CONFIG.API_BASE_URL}/health`);
        const health = await response.json();

        if (health.status === 'UP') {
            console.log('✅ API funcionando correctamente:', health);
        } else {
            console.warn('⚠️ La API reporta problemas:', health);
        }
    } catch (error) {
        console.error('❌ Error verificando la API:', error);
    }
}
