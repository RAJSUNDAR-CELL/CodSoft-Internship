package com.mycompany.currencyconvertor;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Currencyconvertor {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean continueConversion = true;

        System.out.println("================================");
        System.out.println("       CURRENCY CONVERTER");
        System.out.println("================================");

        while (continueConversion) {
            try {
                System.out.println("Available Currencies:");
                System.out.println("1. USD");
                System.out.println("2. INR");
                System.out.println("3. EUR");
                System.out.println("4. GBP");

                System.out.print("\nEnter Source Currency: ");
                String from = scanner.next().toUpperCase();

                System.out.print("Enter Target Currency: ");
                String to = scanner.next().toUpperCase();

                System.out.print("Enter Amount: ");
                double amount = scanner.nextDouble();

                double rate = getExchangeRate(from, to);

                if (rate == -1) {
                    System.out.println("Invalid currency conversion!");
                } else {
                    double convertedAmount = amount * rate;

                    System.out.println("\n===== Conversion Result =====");
                    System.out.println("From Currency : " + from);
                    System.out.println("To Currency   : " + to);
                    System.out.println("Amount        : " + amount);
                    System.out.println("Exchange Rate : " + rate);
                    System.out.printf("Converted Amount: %.2f %s%n",
                            convertedAmount, to);
                }

            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input! Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input from scanner buffer
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                scanner.nextLine();
            }

            System.out.print("\nDo you want to perform another conversion? (Y/N): ");
            if (scanner.hasNext()) {
                String choice = scanner.next().toUpperCase();
                if (!choice.equals("Y") && !choice.equals("YES")) {
                    continueConversion = false;
                }
            } else {
                continueConversion = false;
            }
            System.out.println();
        }

        System.out.println("Thank you for using the Currency Converter.");
        scanner.close();
    }

    public static double getExchangeRate(String from, String to) {

        if (from.equals(to))
            return 1.0;

        if (from.equals("USD") && to.equals("INR"))
            return 86.0;

        if (from.equals("INR") && to.equals("USD"))
            return 0.0116;

        if (from.equals("USD") && to.equals("EUR"))
            return 0.85;

        if (from.equals("EUR") && to.equals("USD"))
            return 1.18;

        if (from.equals("USD") && to.equals("GBP"))
            return 0.74;

        if (from.equals("GBP") && to.equals("USD"))
            return 1.35;

        if (from.equals("INR") && to.equals("EUR"))
            return 0.0099;

        if (from.equals("EUR") && to.equals("INR"))
            return 101.0;

        if (from.equals("INR") && to.equals("GBP"))
            return 0.0086;

        if (from.equals("GBP") && to.equals("INR"))
            return 116.0;

        if (from.equals("EUR") && to.equals("GBP"))
            return 0.87;

        if (from.equals("GBP") && to.equals("EUR"))
            return 1.15;

        return -1;
    }
}
