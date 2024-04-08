package com.bankapp.app.entity;

import com.bankapp.app.enums.CardStatus;
import com.bankapp.app.enums.CardType;
import com.bankapp.app.enums.PaymentSystem;
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
 * <p>
 * Банковская карта - это финансовый инструмент,
 * который клиенты банка используют для осуществления платежей,
 * снятия наличных денег, онлайн-транзакций и других финансовых операций.
 * <p>
 * ----- English -------
 * <p>
 * A bank card is a financial instrument that bank clients use
 * for making payments, withdrawing cash, online transactions,
 * and other financial operations.
 */
public class Card {

    /**
     * ----- Russian ------
     * <p>
     * Идентификации уникальной записи или объекта в базе данных.
     * <p>
     * ----- English -------
     * <p>
     * Unique identifier of the record or object in the database.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    /**
     * ----- Russian ------
     * <p>
     * Номер карты
     * <p>
     * ----- English -------
     * <p>
     * Card number.
     */
    @Column(name = "card_number")
    private String number;

    /**
     * ----- Russian ------
     * <p>
     * Срок действия карты
     * <p>
     * ----- English -------
     * <p>
     * Card expiration date.
     */

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    /**
     * ----- Russian ------
     * <p>
     * Дата и время создания карты.
     * <p>
     * ----- English -------
     * <p>
     * Date and time of card creation.
     */
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    /**
     * ----- Russian ------
     * <p>
     * Дата и временя последнего обновления записи в базе данных.
     * <p>
     * ----- English -------
     * <p>
     * Date and time of the last update to the database record.
     */
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    /**
     * ----- Russian ------
     * <p>
     * Лимит на сумму транзакций, которые могут быть совершены с данной
     * банковской карты.
     * <p>
     * ----- English -------
     * <p>
     * Limit on the amount of transactions that can be made with this bank card.
     */
    @Column(name = "transaction_limit")
    private BigDecimal cardTransactionLimit;

    /**
     * ----- Russian ------
     * <p>
     * Тип банковской карты, кредитная карта, дебетовая карта и т.д.
     * <p>
     * ----- English -------
     * <p>
     * Type of bank card, such as credit card, debit card, etc.
     */
    @Column(name = "card_type")
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    /**
     * ----- Russian ------
     * <p>
     * Указывает на платежную систему, к которой относится данная банковская карта.
     * <p>
     * ----- English -------
     * <p>
     * Indicates the payment system to which this bank card belongs.
     */
    @Column(name = "payment_system")
    @Enumerated(EnumType.STRING)
    private PaymentSystem cardPaymentSystem;

    /**
     * ----- Russian ------
     * <p>
     * Указывает на текущий статус банковской карты
     * <p>
     * ----- English -------
     * <p>
     * Indicates the current status of the bank card.
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CardStatus cardStatus;

    /**
     * ----- Russian ------
     * <p>
     * Это ссылка на связанный счет, к которому привязана данная банковская карта.
     * Он обозначает связь между банковской картой и банковским счетом,
     * на котором хранятся деньги. Благодаря этой связи система знает,
     * с каким счетом ассоциированы финансовые операции,
     * производимые с помощью данной карты.
     * <p>
     * ----- English -------
     * <p>
     * This is a reference to the associated account to which this bank card is linked.
     * It denotes the relationship between the bank card and the bank account
     * where the money is stored. Thanks to this link, the system knows
     * which account is associated with financial operations performed with this card.
     */

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @JsonBackReference
    private Account account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card card)) return false;
        return Objects.equals(number, card.number) && Objects.equals(account, card.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, account);
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", cardNumber='" + number + '\'' +
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


