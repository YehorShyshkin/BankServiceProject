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
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "currency_code")
    @Enumerated(EnumType.STRING)
    private CurrencyCode currencyCode;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY,
            orphanRemoval = true, cascade = {MERGE, PERSIST, REFRESH})
    private List<Agreement> agreementList;

    @OneToMany(mappedBy = "debitAccount", fetch = FetchType.LAZY,
            orphanRemoval = true, cascade = {MERGE, PERSIST, REFRESH})
    private Set<Transaction> debitTransaction;

    @OneToMany(mappedBy = "creditAccount", fetch = FetchType.LAZY,
            orphanRemoval = true, cascade = {MERGE, PERSIST, REFRESH})
    private Set<Transaction> creditTransaction;

    public Account(UUID id, String name, AccountType type,
                   AccountStatus status, CurrencyCode currencyCode,
                   BigDecimal balance, Timestamp createdAt,
                   Timestamp updatedAt, Client client,
                   List<Agreement> agreementList,
                   Set<Transaction> debitTransaction,
                   Set<Transaction> creditTransaction) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
        this.currencyCode = currencyCode;
        this.balance = balance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.client = client;
        this.agreementList = agreementList;
        this.debitTransaction = debitTransaction;
        this.creditTransaction = creditTransaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(name, account.name) && status == account.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", currencyCode=" + currencyCode +
                ", balance=" + balance +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", clients=" + client +
                ", agreementList=" + agreementList +
                ", debitTransaction=" + debitTransaction +
                ", creditTransaction=" + creditTransaction +
                '}';
    }
}
