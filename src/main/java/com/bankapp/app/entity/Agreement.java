package com.bankapp.app.entity;

import com.bankapp.app.enums.AccountStatus;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class Agreement {
    private UUID id; // почитать
    private UUID accountId;
    private Integer productId;
    private double interestRate;
    private AccountStatus status;
    private double sum;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Agreement(UUID id, UUID accountId, Integer productId,
                     double interestRate, AccountStatus status,
                     double sum, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.accountId = accountId;
        this.productId = productId;
        this.interestRate = interestRate;
        this.status = status;
        this.sum = sum;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agreement agreement)) return false;
        return Objects.equals(id, agreement.id) && Objects.equals(accountId, agreement.accountId) && Objects.equals(productId, agreement.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, productId);
    }

    @Override
    public String toString() {
        return "Agreement{" +
                "id=" + id +
                ", account_id=" + accountId +
                ", product_id=" + productId +
                ", interest_rate=" + interestRate +
                ", status=" + status +
                ", sum=" + sum +
                ", created_at=" + createdAt +
                ", updated_at=" + updatedAt +
                '}';
    }
}
