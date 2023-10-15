package com.bankapp.app.entity;

import com.bankapp.app.enums.AccountStatus;
import com.bankapp.app.enums.AccountType;
import com.bankapp.app.enums.CurrencyCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String accountName;

    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Column(name = "currency_code")
    @Enumerated(EnumType.STRING)
    private CurrencyCode currencyCode;

    @Column(name = "balance")
    private BigDecimal accountBalance;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @OneToMany(mappedBy = "account")
    private List<Agreement> agreementList;

    @OneToMany(mappedBy = "debitAccount")
    private Set<Transaction> debitTransaction;

    @OneToMany(mappedBy = "creditAccount")
    private Set<Transaction> creditTransaction;

    public Account(UUID id, String accountName, AccountType accountType,
                   AccountStatus accountStatus, CurrencyCode currencyCode,
                   BigDecimal accountBalance, Timestamp createdAt,
                   Timestamp updatedAt, Client client,
                   List<Agreement> agreementList,
                   Set<Transaction> debitTransaction,
                   Set<Transaction> creditTransaction) {
        this.id = id;
        this.accountName = accountName;
        this.accountType = accountType;
        this.accountStatus = accountStatus;
        this.currencyCode = currencyCode;
        this.accountBalance = accountBalance;
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
        return Objects.equals(accountName, account.accountName) && accountStatus == account.accountStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountName, accountStatus);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + accountName + '\'' +
                ", type=" + accountType +
                ", status=" + accountStatus +
                ", currencyCode=" + currencyCode +
                ", balance=" + accountBalance +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", clients=" + client +
                ", agreementList=" + agreementList +
                ", debitTransaction=" + debitTransaction +
                ", creditTransaction=" + creditTransaction +
                '}';
    }
}
