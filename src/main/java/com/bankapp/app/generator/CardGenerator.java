package com.bankapp.app.generator;

import com.bankapp.app.enums.PaymentSystem;
import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.time.LocalDate;

@UtilityClass
public class CardGenerator {
    public static String generateCardNumber(PaymentSystem paymentSystem) {
        SecureRandom secureRandom = new SecureRandom();
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
            cardNumber.append(secureRandom.nextInt(10));
        }
        String cardNumberWithoutCheckDigit = cardNumber.toString();
        int checkDigit = calculateLuanCheckDigit(cardNumberWithoutCheckDigit);
        cardNumber.append(checkDigit);
        return cardNumber.toString();
    }

    private static int calculateLuanCheckDigit(String cardNumber) {
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

    /**
     * SecureRandom - это класс в языке Java, предоставляющий генерацию криптографически стойких
     * случайных чисел. Он является частью пакета java.security и предоставляет
     * более высокий уровень безопасности по сравнению с обычным Random.
     */
    public String generateCardCVV(){
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder cardCVV = new StringBuilder();
        for (int i = 0; i<3; i++){
            cardCVV.append(secureRandom.nextInt(10));
        }
        return String.valueOf(cardCVV);
    }

    public static LocalDate generateCardExpirationDate() {
        return LocalDate.now().plusYears(5);
    }
}

