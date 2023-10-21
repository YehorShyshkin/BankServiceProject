package com.bankapp.app.entity;

import com.bankapp.app.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "agreements")
@NoArgsConstructor
@Getter
@Setter
public class Agreement {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    @Column(name = "interest_rate")
    private BigDecimal agreementInterestRate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus agreementStatus;

    @Column(name = "sum")
    private BigDecimal agreementSum;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    public Agreement(UUID id, BigDecimal agreementInterestRate, AccountStatus agreementStatus,
                     BigDecimal agreementSum, Timestamp createdAt,
                     Timestamp updatedAt,
                     Product product,
                     Account account) {
        this.id = id;
        this.agreementInterestRate = agreementInterestRate;
        this.agreementStatus = agreementStatus;
        this.agreementSum = agreementSum;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.product = product;
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agreement agreement)) return false;
        return agreementStatus == agreement.agreementStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(agreementStatus);
    }

    @Override
    public String toString() {
        return "Agreement{" +
                "id=" + id +
                ", interestRate=" + agreementInterestRate +
                ", status=" + agreementStatus +
                ", sum=" + agreementSum +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", products=" + product +
                ", accounts=" + account +
                '}';
    }
}
