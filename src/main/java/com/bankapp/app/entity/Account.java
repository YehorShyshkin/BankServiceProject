package com.bankapp.app.entity;

import com.bankapp.app.enums.AccountStatus;
import com.bankapp.app.enums.AccountType;
import com.bankapp.app.enums.CurrencyCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

/**
 * ----- Russian ------
 * Это сущность, которая представляет собой аккаунт (счет) в банковской системе.
 * Эта сущность содержит информацию о конкретном счете и связанных с ним данных.
 * <p>
 * ----- English -------
 * This entity represents an account in the banking system.
 * It contains information about a specific account and related data.
 */
public class Account {

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
     * Это название счета, которое помогает идентифицировать его и понять его предназначение.
     * <p>
     * ----- English -------
     * The name of the account, helping to identify it and understand its purpose.
     */
    @Column(name = "name")
    private String accountName;


    /**
     * ---- Russian -------
     * Тип счета, который может указывать на его характеристики,
     * такие как "сберегательный счет", "текущий счет" и т. д.
     * <p>
     * ----- English -------
     * The type of the account, which may indicate its characteristics,
     * such as "savings account," "checking account," etc.
     */
    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    /**
     * ---- Russian -------
     * Статус счета, который может указывать на его состояние, такие как "активен", "закрыт" и т. д.
     * <p>
     * ----- English -------
     * The status of the account, which may indicate its state, such as "active," "closed," etc.
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    /**
     * ---- Russian -------
     * Код валюты, в которой ведется учет средств на счете, например, "USD" или "EUR".
     * <p>
     * ----- English -------
     * The currency code in which funds in the account are tracked, e.g., "USD" or "EUR."
     */
    @Column(name = "currency_code")
    @Enumerated(EnumType.STRING)
    private CurrencyCode currencyCode;

    /**
     * ---- Russian -------
     * Баланс (остаток) средств на счете.
     * <p>
     * ----- English -------
     * The balance (remaining funds) in the account.
     */
    @Column(name = "balance")
    private BigDecimal accountBalance;

    /**
     * ---- Russian -------
     * Дата и время создания счета.
     * <p>
     * ----- English -------
     * Date and time of account creation.
     */
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    /**
     * ---- Russian -------
     * Дата и временя последнего обновления записи в базе данных.
     * <p>
     * ----- English -------
     * Date and time of the last update to the database record.
     */
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    /**
     * ---- Russian -------
     * Клиент, связанный с данным счетом.
     * <p>
     * ----- English -------
     * The client associated with this account.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    /**
     * ---- Russian -------
     * Список договоров или соглашений, связанных с этим счетом.
     * <p>
     * ----- English -------
     * The list of agreements associated with this account.
     */
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private List<Agreement> agreementList;

    /**
     * ---- Russian -------
     * Это операции списания средств с банковского счета или карточки.
     * <p>
     * ----- English -------
     * Operations representing withdrawal of funds from the bank account or card.
     */
    @OneToMany(mappedBy = "transactionDebitAccount")
    private Set<Transaction> debitTransaction;

    /**
     * ---- Russian -------
     * Это финансовая операция, при которой средства перечисляются на банковский счет или кредитную/дебетовую карту клиента.
     * <p>
     * ----- English -------
     * Financial operation where funds are transferred to the client's bank account or credit/debit card.
     */
    @OneToMany(mappedBy = "transactionCreditAccount")
    private Set<Transaction> creditTransaction;

    /**
     * ---- Russian -------
     * Представляет собой класс с различными свойствами, представляющими информацию о банковской карте.
     * <p>
     * ----- English -------
     * Represents a class with various properties representing information about a bank card.
     */

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Card> cards;

    public Account(UUID id, String accountName, AccountType accountType, AccountStatus accountStatus,
                   CurrencyCode currencyCode, BigDecimal accountBalance, Timestamp createdAt, Timestamp updatedAt,
                   Client client, List<Agreement> agreementList, Set<Transaction> debitTransaction,
                   Set<Transaction> creditTransaction, List<Card> cards) {
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
        this.cards = cards;
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
                ", accountName='" + accountName + '\'' +
                ", accountType=" + accountType +
                ", accountStatus=" + accountStatus +
                ", currencyCode=" + currencyCode +
                ", accountBalance=" + accountBalance +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", client=" + client +
                '}';
    }
}
