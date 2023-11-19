package com.bankapp.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

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
