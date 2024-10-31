package com.bankapp.app.generator;

import com.bankapp.app.enums.PaymentSystem;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.Random;

@UtilityClass
public class CardGenerator {
    public static String generateCardNumber(PaymentSystem paymentSystem) {
        Random random = new Random();
        String iin = switch (paymentSystem) {
            case VISA -> "4";
            case MASTERCARD -> "5";
            case AMERICAN_EXPRESS -> "3";
            case PAYPAL -> "6";
            case APPLE_PAY -> "7";
            case GOOGLE_PAY -> "8";
            case SEPA -> "9";
        };
        StringBuilder cardNumber = new StringBuilder(iin);
        for (int i = 0; i < 15 - iin.length(); i++) {
            cardNumber.append(random.nextInt(10));
        }
        String cardNumberWithoutCheckDigit = cardNumber.toString();
        int checkDigit = calculateLuhnCheckDigit(cardNumberWithoutCheckDigit);
        cardNumber.append(checkDigit);
        return cardNumber.toString();
    }

    private static int calculateLuhnCheckDigit(String cardNumber) {
        int sum = 0;
        boolean doubleDigit = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
                sum += digit;
                doubleDigit = !doubleDigit;
            }
        }
        int mod = sum % 10;
        if (mod == 0) {
            return 0;
        } else {
            return 10 - mod;
        }
    }

    public static LocalDate generateCardExpirationDate() {
        return LocalDate.now().plusYears(5);
    }
}

