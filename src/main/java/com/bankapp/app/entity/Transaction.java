package com.bankapp.app.entity;

import com.bankapp.app.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "transaction_id")
    private UUID id;

    @Column(name = "transaction_type")
    private TransactionType type;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debit_account_id")
    private Account debitAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_account_id")
    private Account creditAccount;

    public Transaction(UUID id, TransactionType type,
                       BigDecimal amount, String description,
                       Timestamp createdAt, Account debitAccount,
                       Account creditAccount) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", debitAccount=" + debitAccount +
                ", creditAccount=" + creditAccount +
                '}';
    }
}
