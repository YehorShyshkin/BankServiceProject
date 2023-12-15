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

    /**
     * Account CreditAccount
     */

    private UUID creditAccountId;
}
