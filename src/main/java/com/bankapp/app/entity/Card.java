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
public class Card {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "holder_name")
    private String holderName;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "balance")
    private BigDecimal cardBalance;

    @Column(name = "transaction_limit")
    private BigDecimal cardTransactionLimit;

    @Column(name = "delivery_point")
    private String cardDeliveryPoint;

    @Column(name = "is_digital_wallet")
    private boolean cardDigitalWallet;

    @Column(name = "is_virtual_wallet")
    private boolean cardVirtualWallet;

    @Column(name = "co_brand")
    private String cardCoBrand;

    @Column(name = "payment_system")
    @Enumerated(EnumType.STRING)
    private PaymentSystem cardPaymentSystem;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CardStatus cardStatus;

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
