package com.bankapp.app.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class Transaction {

    private UUID id;
    private int type;
    private double amount;
    private String description;
    private Timestamp created_at;
    private Account debitAccountId;
    private Account creditAccountId;

    public Transaction(UUID id, int type, double amount, String description,
                       Timestamp created_at, Account debitAccountId,
                       Account creditAccountId) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.created_at = created_at;
        //this.debitAccountId = debitAccountId.getId();
        //this.creditAccountId = creditAccountId.getClient_id();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description) && Objects.equals(debitAccountId, that.debitAccountId) && Objects.equals(creditAccountId, that.creditAccountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, debitAccountId, creditAccountId);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", created_at=" + created_at +
                ", debit_account_id=" + debitAccountId +
                ", credit_account_id=" + creditAccountId +
                '}';
    }
}
