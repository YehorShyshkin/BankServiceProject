package com.bankapp.app.entity;

import com.bankapp.app.enums.CardStatus;
import com.bankapp.app.enums.CardType;
import com.bankapp.app.enums.PaymentSystem;
import com.bankapp.app.generator.CardGenerator;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.UUID;

@Entity
@Table(name = "cards")
@NoArgsConstructor
@Getter
@Setter
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
     * Лимит на сумму транзакций, которые могут быть совершены с данной
     * банковской карты.
     */
    @Column(name = "transaction_limit")
    private BigDecimal cardTransactionLimit;

    /**
     * Тип банковской карты, кредитная карта, дебетовая карта и т.д.
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


