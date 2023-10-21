package com.bankapp.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransactionDTO {

    /**
     * Transaction
     */

    private String transactionType;
    private String transactionAmount;
    private String transactionDescription;
    private String transactionCreatedAt;

    /**
     * Account DebitAccount
     */

    private String debitAccountName;
    private String debitAccountType;
    private String debitAccountStatus;
    private String debitAccountCurrencyCode;
    private String debitAccountBalance;

    /**
     * Client DebitClient
     */

    private String debitClientStatus;
    private String debitClientLastName;
    /**
     * Account CreditAccount
     */

    private String creditAccountName;
    private String creditAccountType;
    private String creditAccountStatus;
    private String creditAccountCurrencyCode;
    private String creditAccountBalance;

    /**
     * Client CreditClient
     */

    private String creditClientStatus;
    private String creditClientLastName;


}
