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
 * <p>
 * Этот класс представляет сущность "транзакция"
 * <p>
 * ----- English -------
 * <p>
 * This class represents the "Transaction".
 */
public class Transaction {

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
     * Тип транзакции, представленный перечислением TransactionType.
     * <p>
     * ----- English -------
     * <p>
     * The type of the transaction, represented by the TransactionType enumeration.
     */
    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    /**
     * ----- Russian ------
     * <p>
     * Сумма транзакции.
     * <p>
     * ----- English -------
     * <p>
     * The amount of the transaction.
     */
    @Column(name = "amount")
    private BigDecimal amount;

    /**
     * ----- Russian ------
     * <p>
     * Описание транзакции.
     * <p>
     * ----- English -------
     * <p>
     * The description of the transaction.
     */
    @Column(name = "description")
    private String description;

    /**
     * ----- Russian ------
     * <p>
     * Дата и время создания транзакции
     * <p>
     * ----- English -------
     * <p>
     * Date and time of the transaction creation.
     */
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    /**
     * ----- Russian ------
     * <p>
     * Ссылка на счет, связанный с дебетовой стороной транзакции.
     * <p>
     * ----- English -------
     * <p>
     * Reference to the account associated with the debit side of the transaction.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "debit_account_id", referencedColumnName = "id")
    private Account debitAccount;

    /**
     * ----- Russian ------
     * <p>
     * Ссылка на счет, связанный с кредитной стороной транзакции.
     * <p>
     * ----- English -------
     * <p>
     * Reference to the account associated with the credit side of the transaction.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "credit_account_id", referencedColumnName = "id")
    private Account creditAccount;

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
                ", debitAccount=" + debitAccount.getName() +
                ", creditAccount=" + creditAccount.getName() +
                '}';
    }
}
