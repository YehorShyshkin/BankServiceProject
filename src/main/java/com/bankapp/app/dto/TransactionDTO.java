package com.bankapp.app.dto;

import lombok.*;

import java.util.UUID;

@Data
public class TransactionDTO {

    /**
     * Transaction
     */

    private String transactionType;
    private String transactionAmount;
    private String transactionDescription;

    /**
     * Account DebitAccount
     */
    private UUID debitAccountId;
//    private String debitAccountType;
//    private String debitAccountStatus;
//    private String debitAccountCurrencyCode;
//    private String debitAccountBalance;

    /**
     * Account CreditAccount
     */

    private UUID creditAccountId;
//    private String creditAccountType;
//    private String creditAccountStatus;
//    private String creditAccountCurrencyCode;
//    private String creditAccountBalance;

}
