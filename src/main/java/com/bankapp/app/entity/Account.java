package com.bankapp.app.entity;

import com.bankapp.app.enums.AccountStatus;
import com.bankapp.app.enums.AccountType;
import com.bankapp.app.enums.CurrencyCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@ToString
/**
 * Это сущность, которая представляет собой аккаунт (счет) в банковской системе.
 * Эта сущность содержит информацию о конкретном счете и связанных с ним данных.
 */
public class Account {

    /**
     * Идентификации уникальной записи или объекта в базе данных
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    /**
     * Это название счета,
     * которое помогает идентифицировать его и понять его предназначение.
     */
    @Column(name = "name")
    private String accountName;


    /**
     * Тип счета, который может указывать на его характеристики,
     * такие как "сберегательный счет", "текущий счет" и т. д.
     */
    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    /**
     * Статус счета, который может указывать на его состояние,
     * такие как "активен", "закрыт" и т. д.
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    /**
     * Код валюты, в которой ведется учет средств на счете,
     * например, "USD" или "EUR".
     */
    @Column(name = "currency_code")
    @Enumerated(EnumType.STRING)
    private CurrencyCode currencyCode;

    /**
     * Баланс (остаток) средств на счете.
     */
    @Column(name = "balance")
    private BigDecimal accountBalance;

    /**
     * Дата и время создания счета.
     */
    @Column(name = "created_at")
    private Timestamp createdAt;

    /**
     * Дата и временя последнего обновления записи в базе данных.
     */
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    /**
     * Клиент, связанный с данным счетом.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    /**
     * Список договоров или соглашений, связанных с этим счетом.
     */
    @OneToMany(mappedBy = "account")
    private List<Agreement> agreementList;

    /**
     * Это операции списания средств с банковского счета или карточки
     */
    @OneToMany(mappedBy = "transactionDebitAccount")
    private Set<Transaction> debitTransaction;

    /**
     * Это финансовая операция, при которой средства перечисляются
     * на банковский счет или кредитную/дебетовую карту клиента
     */
    @OneToMany(mappedBy = "transactionCreditAccount")
    private Set<Transaction> creditTransaction;

    /**
     * Представляет собой класс с различными свойствами,
     * представляющими информацию о банковской карте.
     */
    @OneToOne(mappedBy = "account", fetch = FetchType.EAGER)
    private Card accountCard;

    public Account(UUID id, String accountName, AccountType accountType, AccountStatus accountStatus, CurrencyCode currencyCode, BigDecimal accountBalance, Timestamp createdAt, Timestamp updatedAt, Client client, List<Agreement> agreementList, Set<Transaction> debitTransaction, Set<Transaction> creditTransaction, Card accountCard) {
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
        this.accountCard = accountCard;
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
}
