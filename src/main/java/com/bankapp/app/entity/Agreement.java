package com.bankapp.app.entity;

import com.bankapp.app.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "agreements")
@NoArgsConstructor
@Getter
@Setter
/**
 * ---- Russian -------
 * Представлять собой информацию о банковских соглашениях,
 * связанных с банковскими картами или аккаунтами.
 * <p>
 * ----- English -------
 * Represents information about banking agreements
 * related to bank cards or accounts.
 */
public class Agreement {

    /**
     * ---- Russian -------
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
     * ---- Russian -------
     * Процентная ставка, применимая к соглашению.
     * <p>
     * ----- English -------
     * Interest rate applicable to the agreement.
     */
    @Column(name = "interest_rate")
    private BigDecimal agreementInterestRate;

    /**
     * ---- Russian -------
     * Текущий статус соглашения (например, активное, закрытое и т. д.).
     * <p>
     * ----- English -------
     * Current status of the agreement (e.g., active, closed, etc.).
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus agreementStatus;

    /**
     * ---- Russian -------
     * Сумма соглашения, то есть сумма денег, оговоренная в соглашении.
     * <p>
     * ----- English -------
     * Sum of the agreement, i.e., the amount of money specified in the agreement.
     */
    @Column(name = "sum")
    private BigDecimal agreementSum;

    /**
     * ---- Russian -------
     * Дата и время создания записи о соглашении.
     * <p>
     * ----- English -------
     * Date and time of the agreement record creation.
     */
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    /**
     * ---- Russian -------
     * Дата и время последнего обновления записи о соглашении.
     * <p>
     * ----- English -------
     * Date and time of the last update to the agreement record.
     */
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    /**
     * ---- Russian -------
     * Означает продукт или услугу, предлагаемую банком
     * <p>
     * ----- English -------
     * Indicates the product or service offered by the bank.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    /**
     * ---- Russian -------
     * Это поле используется для хранения информации о банковском счете,
     * связанном с объектом данного класса.
     * <p>
     * ----- English -------
     * This field is used to store information about the bank account
     * associated with the object of this class.
     */
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
