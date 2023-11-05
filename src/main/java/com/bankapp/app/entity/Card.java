package com.bankapp.app.entity;

import com.bankapp.app.enums.CardStatus;
import com.bankapp.app.enums.CardType;
import com.bankapp.app.enums.PaymentSystem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "cards")
@NoArgsConstructor
@Getter
@Setter
@ToString
/**
 * Банковская карта - это финансовый инструмент,
 * который клиенты банка используют для осуществления платежей,
 * снятия наличных денег, онлайн-транзакций и других финансовых операций.
 */
public class Card {

    /**
     * Идентификации уникальной записи или объекта в базе данных
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    /**
     * Номер карты
     */
    @Column(name = "card_number")
    private String cardNumber;

    /**
     * Срок действия карты
     */

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    /**
     * Дата и время создания карты.
     */
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    /**
     * Дата и временя последнего обновления записи в базе данных.
     */
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    /**
     * Баланс карты
     */
    @Column(name = "balance")
    private BigDecimal cardBalance;

    /**
     * Лимит на сумму транзакций, которые могут быть совершены с данной
     * банковской карты.
     */
    @Column(name = "transaction_limit")
    private BigDecimal cardTransactionLimit;

    /**
     * Является ли банковская карта цифровым кошельком
     */
    @Column(name = "card_type")
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    /**
     * Указывает на платежную систему, к которой относится данная банковская карта.
     */
    @Column(name = "payment_system")
    @Enumerated(EnumType.STRING)
    private PaymentSystem cardPaymentSystem;

    /**
     * Указывает на текущий статус банковской карты
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CardStatus cardStatus;

    /**
     * Это ссылка на связанный счет, к которому привязана данная банковская карта.
     * Он обозначает связь между банковской картой и банковским счетом,
     * на котором хранятся деньги. Благодаря этой связи система знает,
     * с каким счетом ассоциированы финансовые операции,
     * производимые с помощью данной карты.
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    public Card(UUID id, String cardNumber, LocalDate expirationDate, Timestamp createdAt, Timestamp updatedAt, BigDecimal cardBalance, BigDecimal cardTransactionLimit, CardType cardType, PaymentSystem cardPaymentSystem, CardStatus cardStatus, Account account) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.cardBalance = cardBalance;
        this.cardTransactionLimit = cardTransactionLimit;
        this.cardType = cardType;
        this.cardPaymentSystem = cardPaymentSystem;
        this.cardStatus = cardStatus;
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card card)) return false;
        return Objects.equals(cardNumber, card.cardNumber) && Objects.equals(account, card.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, account);
    }

    public String generateCardNumber(PaymentSystem paymentSystem) {
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
            cardNumber.append(random.nextInt(10)); // Генерация случайной цифры от 0 до 9
        }

        String cardNumberWithoutCheckDigit = cardNumber.toString();
        int checkDigit = calculateLuhnCheckDigit(cardNumberWithoutCheckDigit);
        cardNumber.append(checkDigit);

        return cardNumber.toString();
    }

    private int calculateLuhnCheckDigit(String cardNumber) {
        int sum = 0;
        boolean doubleDigit = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            doubleDigit = !doubleDigit;
        }

        int mod = sum % 10;
        if (mod == 0) {
            return 0;
        } else {
            return 10 - mod;
        }
    }

    public LocalDate generateCardExpirationDate() {
        int yearsToAdd = 5;
        // Получаем текущую дату
        LocalDate currentDate = LocalDate.now();

        // Добавляем указанное количество лет к текущей дате
        LocalDate expirationDate = currentDate.plusYears(yearsToAdd);

        return expirationDate;
    }
}


