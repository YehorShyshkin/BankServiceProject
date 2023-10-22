package com.bankapp.app.entity;

import com.bankapp.app.enums.CardStatus;
import com.bankapp.app.enums.PaymentSystem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
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
     * Имя держателя
     */
    @Column(name = "holder_name")
    private String holderName;

    /**
     * Срок действия карты
     */

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

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
     * Имя получателя денег
     */
    @Column(name = "delivery_point")
    private String cardDeliveryPoint;

    /**
     * Является ли банковская карта цифровым кошельком
     */
    @Column(name = "is_digital_wallet")
    private boolean cardDigitalWallet;

    /**
     * Является ли банковская карта виртуальным кошельком
     */
    @Column(name = "is_virtual_wallet")
    private boolean cardVirtualWallet;

    /**
     * Через что была проведена транзакция
     */
    @Column(name = "co_brand")
    private String cardCoBrand;

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

    public Card(UUID id, String cardNumber, String holderName, LocalDate expirationDate, BigDecimal cardBalance, BigDecimal cardTransactionLimit, String cardDeliveryPoint, boolean cardDigitalWallet, boolean cardVirtualWallet, String cardCoBrand, PaymentSystem cardPaymentSystem, CardStatus cardStatus, Account account) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.holderName = holderName;
        this.expirationDate = expirationDate;
        this.cardBalance = cardBalance;
        this.cardTransactionLimit = cardTransactionLimit;
        this.cardDeliveryPoint = cardDeliveryPoint;
        this.cardDigitalWallet = cardDigitalWallet;
        this.cardVirtualWallet = cardVirtualWallet;
        this.cardCoBrand = cardCoBrand;
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

}
