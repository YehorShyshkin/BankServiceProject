package com.bankapp.app.entity;

import com.bankapp.app.enums.AccountStatus;
import com.bankapp.app.enums.AccountType;
import com.bankapp.app.enums.CurrencyCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "account")
@NoArgsConstructor
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(generator = "UUID")

    @Column(name = "id")
    private UUID id;

    @Column(name = "client_id")
    private UUID clientId;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private AccountType type;

    @Column(name = "status")
    private AccountStatus status;

    @Column(name = "balance")
    private float balance;

    @Column(name = "currency_code")
    private CurrencyCode currencyCode;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public Account(UUID id, UUID clientId, String name,
                   AccountType type, AccountStatus status,
                   float balance, CurrencyCode currency_code,
                   Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.clientId = clientId;
        this.name = name;
        this.type = type;
        this.status = status;
        this.balance = balance;
        this.currencyCode = currency_code;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(id, account.id)
                && Objects.equals(clientId, account.clientId)
                && Objects.equals(name, account.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, name);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", client_id=" + clientId +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", balance=" + balance +
                ", currency_code=" + currencyCode +
                ", created_at=" + createdAt +
                ", updated_at=" + updatedAt +
                '}';
    }
}
