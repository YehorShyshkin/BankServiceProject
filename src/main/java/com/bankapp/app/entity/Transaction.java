package com.bankapp.app.entity;

import com.bankapp.app.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@NoArgsConstructor
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "amount")
    private BigDecimal transactionAmount;

    @Column(name = "description")
    private String transactionDescription;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp transactionCreatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "debit_account_id", referencedColumnName = "id")
    private Account transactionDebitAccount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "credit_account_id", referencedColumnName = "id")
    private Account transactionCreditAccount;

    public Transaction(UUID id, TransactionType transactionType,
                       BigDecimal transactionAmount, String transactionDescription,
                       Timestamp transactionCreatedAt, Account transactionDebitAccount,
                       Account transactionCreditAccount) {
        this.id = id;
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
        this.transactionDescription = transactionDescription;
        this.transactionCreatedAt = transactionCreatedAt;
        this.transactionDebitAccount = transactionDebitAccount;
        this.transactionCreditAccount = transactionCreditAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return transactionType == that.transactionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionType);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type=" + transactionType +
                ", amount=" + transactionAmount +
                ", description='" + transactionDescription + '\'' +
                ", createdAt=" + transactionCreatedAt +
                ", debitAccount=" + transactionDebitAccount +
                ", creditAccount=" + transactionCreditAccount +
                '}';
    }
}
