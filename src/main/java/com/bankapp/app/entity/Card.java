package com.bankapp.app.entity;

import com.bankapp.app.enums.CardStatus;
import com.bankapp.app.enums.CardType;
import com.bankapp.app.enums.PaymentSystem;
import com.bankapp.app.generator.CardGenerator;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "cards")
@NoArgsConstructor
@Getter
@Setter
/**
 * ---- Russian -------
 * Банковская карта - это финансовый инструмент,
 * который клиенты банка используют для осуществления платежей,
 * снятия наличных денег, онлайн-транзакций и других финансовых операций.
 * <p>
 * ----- English -------
 * A bank card is a financial instrument that bank clients use
 * for making payments, withdrawing cash, online transactions,
 * and other financial operations.
 */
public class Card {

    /**
     * ----- Russian ------
     * Идентификации уникальной записи или объекта в базе данных.
     * <p>
     * ----- English -------
     * Unique identifier of the record or object in the database.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    /**
     * ----- Russian ------
     * Номер карты
     * <p>
     * ----- English -------
     * Card number.
     */
    @Column(name = "card_number")
    private String cardNumber;

    /**
     * ----- Russian ------
     * Срок действия карты
     * <p>
     * ----- English -------
     * Card expiration date.
     */

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    /**
     * ----- Russian ------
     * Дата и время создания карты.
     * <p>
     * ----- English -------
     * Date and time of card creation.
     */
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    /**
     * ----- Russian ------
     * Дата и временя последнего обновления записи в базе данных.
     * <p>
     * ----- English -------
     * Date and time of the last update to the database record.
     */
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    /**
     * ----- Russian ------
     * Лимит на сумму транзакций, которые могут быть совершены с данной
     * банковской карты.
     * <p>
     * ----- English -------
     * Limit on the amount of transactions that can be made with this bank card.
     */
    @Column(name = "transaction_limit")
    private BigDecimal cardTransactionLimit;

    /**
     * ----- Russian ------
     * Тип банковской карты, кредитная карта, дебетовая карта и т.д.
     * <p>
     * ----- English -------
     * Type of bank card, such as credit card, debit card, etc.
     */
    @Column(name = "card_type")
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    /**
     * ----- Russian ------
     * Указывает на платежную систему, к которой относится данная банковская карта.
     * <p>
     * ----- English -------
     * Indicates the payment system to which this bank card belongs.
     */
    @Column(name = "payment_system")
    @Enumerated(EnumType.STRING)
    private PaymentSystem cardPaymentSystem;

    /**
     * ----- Russian ------
     * Указывает на текущий статус банковской карты
     * <p>
     * ----- English -------
     * Indicates the current status of the bank card.
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CardStatus cardStatus;

    /**
     * ----- Russian ------
     * Это ссылка на связанный счет, к которому привязана данная банковская карта.
     * Он обозначает связь между банковской картой и банковским счетом,
     * на котором хранятся деньги. Благодаря этой связи система знает,
     * с каким счетом ассоциированы финансовые операции,
     * производимые с помощью данной карты.
     * <p>
     * ----- English -------
     * This is a reference to the associated account to which this bank card is linked.
     * It denotes the relationship between the bank card and the bank account
     * where the money is stored. Thanks to this link, the system knows
     * which account is associated with financial operations performed with this card.
     */

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
//    @JsonIgnore
    @JsonBackReference
    private Account account;


    public Card(UUID id, String cardNumber, LocalDate expirationDate,
                Timestamp createdAt, Timestamp updatedAt,
                BigDecimal cardTransactionLimit, CardType cardType,
                PaymentSystem cardPaymentSystem, CardStatus cardStatus,
                Account account) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.expirationDate = CardGenerator.generateCardExpirationDate();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", expirationDate=" + expirationDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", cardTransactionLimit=" + cardTransactionLimit +
                ", cardType=" + cardType +
                ", cardPaymentSystem=" + cardPaymentSystem +
                ", cardStatus=" + cardStatus +
                '}';
    }
}


