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

/**
 * ----- Russian ------
 * Этот класс представляет сущность "транзакция"
 * <p>
 * ----- English -------
 * This class represents the "Transaction".
 */
public class Transaction {

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
     * Тип транзакции, представленный перечислением TransactionType.
     * <p>
     * ----- English -------
     * The type of the transaction, represented by the TransactionType enumeration.
     */
    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    /**
     * ----- Russian ------
     * Сумма транзакции.
     * <p>
     * ----- English -------
     * The amount of the transaction.
     */
    @Column(name = "amount")
    private BigDecimal transactionAmount;

    /**
     * ----- Russian ------
     * Описание транзакции.
     * <p>
     * ----- English -------
     * The description of the transaction.
     */
    @Column(name = "description")
    private String transactionDescription;

    /**
     * ----- Russian ------
     * Дата и время создания транзакции
     * <p>
     * ----- English -------
     * Date and time of the transaction creation.
     */
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp transactionCreatedAt;

    /**
     * ----- Russian ------
     * Ссылка на счет, связанный с дебетовой стороной транзакции.
     * <p>
     * ----- English -------
     * Reference to the account associated with the debit side of the transaction.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "debit_account_id", referencedColumnName = "id")
    private Account transactionDebitAccount;

    /**
     * ----- Russian ------
     * Ссылка на счет, связанный с кредитной стороной транзакции.
     * <p>
     * ----- English -------
     * Reference to the account associated with the credit side of the transaction.
     */
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
