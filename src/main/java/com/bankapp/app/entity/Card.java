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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "cards")

public class Card {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "card_number")
    private String number;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "transaction_limit")
    private BigDecimal cardTransactionLimit;

    @Column(name = "card_type")
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Column(name = "payment_system")
    @Enumerated(EnumType.STRING)
    private PaymentSystem paymentSystem;


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CardStatus cardStatus;

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

}


