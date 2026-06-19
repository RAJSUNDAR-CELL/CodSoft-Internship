import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {

    private static final String API_URL = "https://open.er-api.com/v6/latest/USD";

    public static void main(String[] args) {
        try {
            // Fetch exchange rates from the API
            String jsonResponse = fetchExchangeRates(API_URL);

            if (jsonResponse != null) {
                // Initialize Scanner for user input
                Scanner scanner = new Scanner(System.in);

                System.out.println("Welcome to the Currency Converter!");

                // Get amount
                System.out.print("Enter the amount you want to convert: ");
                double amount = -1;
                while (amount < 0) {
                    if (scanner.hasNextDouble()) {
                        amount = scanner.nextDouble();
                        if (amount < 0) {
                            System.out.print("Amount cannot be negative. Please enter a valid amount: ");
                        }
                    } else {
                        System.out.print("Invalid input. Please enter a numerical amount: ");
                        scanner.next(); // consume invalid input
                    }
                }
                scanner.nextLine(); // consume newline

                // Get base currency
                System.out.print("Enter the base currency code (e.g., USD, EUR, INR): ");
                String baseCurrency = scanner.nextLine().toUpperCase();

                // Get target currency
                System.out.print("Enter the target currency code (e.g., USD, EUR, INR): ");
                String targetCurrency = scanner.nextLine().toUpperCase();

                // Extract rates
                double baseRate = extractRate(jsonResponse, baseCurrency);
                double targetRate = extractRate(jsonResponse, targetCurrency);

                if (baseRate == -1 || targetRate == -1) {
                    System.out.println("Error: One or both currency codes are invalid or not found in the exchange rate data.");
                } else {
                    // Perform conversion
                    // The API base is USD. So we first convert the base currency to USD, then USD to target.
                    double amountInUSD = amount / baseRate;
                    double convertedAmount = amountInUSD * targetRate;

                    // Display result
                    System.out.printf("%.2f %s = %.4f %s%n", amount, baseCurrency, convertedAmount, targetCurrency);
                }

                scanner.close();

            } else {
                System.out.println("Failed to fetch exchange rates. Please check your internet connection or the API URL.");
            }

        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String fetchExchangeRates(String urlString) {
        try {
            java.net.URI uri = new java.net.URI(urlString);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching data: " + e.getMessage());
            return null;
        }
    }

    // A simple method to extract the rate for a given currency code from the JSON string
    // This avoids needing an external JSON parsing library for this simple task
    private static double extractRate(String jsonString, String currencyCode) {
        String searchString = "\"" + currencyCode + "\":";
        int index = jsonString.indexOf(searchString);

        if (index != -1) {
            int startIndex = index + searchString.length();
            int endIndex = jsonString.indexOf(",", startIndex);

            // If it's the last element, it might end with } instead of ,
            int endBraceIndex = jsonString.indexOf("}", startIndex);
            if (endIndex == -1 || (endBraceIndex != -1 && endBraceIndex < endIndex)) {
                endIndex = endBraceIndex;
            }

            if (endIndex != -1) {
                String rateString = jsonString.substring(startIndex, endIndex).trim();
                try {
                    return Double.parseDouble(rateString);
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
        }
        return -1; // Currency not found
    }
}