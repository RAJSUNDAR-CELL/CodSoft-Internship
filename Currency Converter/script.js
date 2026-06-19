const amountInput = document.getElementById('amount');
const baseCurrencySelect = document.getElementById('base-currency');
const targetCurrencySelect = document.getElementById('target-currency');
const convertBtn = document.getElementById('convert-btn');
const resultText = document.getElementById('result-text');

let exchangeRates = {};

async function fetchExchangeRates() {
    try {
        const response = await fetch('https://open.er-api.com/v6/latest/USD');
        const data = await response.json();

        if (data.result === "success") {
            exchangeRates = data.rates;
            populateCurrencyDropdowns();
            // Default conversions
            baseCurrencySelect.value = 'USD';
            targetCurrencySelect.value = 'EUR';
        } else {
            console.error("Failed to fetch exchange rates");
            resultText.textContent = "Error fetching rates.";
        }
    } catch (error) {
        console.error("Error:", error);
        resultText.textContent = "Error connecting to API.";
    }
}

function populateCurrencyDropdowns() {
    const currencies = Object.keys(exchangeRates);

    currencies.forEach(currency => {
        const option1 = document.createElement('option');
        option1.value = currency;
        option1.textContent = currency;
        baseCurrencySelect.appendChild(option1);

        const option2 = document.createElement('option');
        option2.value = currency;
        option2.textContent = currency;
        targetCurrencySelect.appendChild(option2);
    });
}

function convertCurrency() {
    const amount = parseFloat(amountInput.value);
    const baseCurrency = baseCurrencySelect.value;
    const targetCurrency = targetCurrencySelect.value;

    if (isNaN(amount) || amount < 0) {
        resultText.textContent = "Please enter a valid amount.";
        return;
    }

    if (!exchangeRates[baseCurrency] || !exchangeRates[targetCurrency]) {
        resultText.textContent = "Exchange rates not available.";
        return;
    }

    // Convert base currency to USD first (since API base is USD)
    const amountInUSD = amount / exchangeRates[baseCurrency];

    // Convert USD to target currency
    const convertedAmount = amountInUSD * exchangeRates[targetCurrency];

    resultText.textContent = `${amount} ${baseCurrency} = ${convertedAmount.toFixed(4)} ${targetCurrency}`;
}

convertBtn.addEventListener('click', convertCurrency);

// Fetch rates on load
fetchExchangeRates();