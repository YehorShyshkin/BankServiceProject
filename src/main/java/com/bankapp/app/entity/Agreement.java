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
 * Представлять собой информацию о банковских соглашениях,
 * связанных с банковскими картами или аккаунтами.
 */
public class Agreement {

    /**
     * Идентификации уникальной записи или объекта в базе данных
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    /**
     * Процентная ставка, применимая к соглашению.
     */
    @Column(name = "interest_rate")
    private BigDecimal agreementInterestRate;

    /**
     * Текущий статус соглашения (например, активное, закрытое и т. д.).
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus agreementStatus;

    /**
     * Сумма соглашения, то есть сумма денег, оговоренная в соглашении.
     */
    @Column(name = "sum")
    private BigDecimal agreementSum;

    /**
     * Дата и время создания записи о соглашении.
     */
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    /**
     * Дата и время последнего обновления записи о соглашении.
     */
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    /**
     * Означает продукт или услугу, предлагаемую банком
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;


    /**
     * Это поле используется для хранения информации о банковском счете,
     * связанном с объектом данного класса.
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
