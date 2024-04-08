package com.bankapp.app.controller.dto;

import lombok.*;

@Data
public class AccountDTO {
    /**
     * Account
     */
    private String accountName;
    private String accountType;
    private String accountStatus;
    private String currencyCode;
    private String accountBalance;

    /**
     * Client
     */
    private String clientStatus;
    private String clientLastName;

    /**
     * Agreement
     */
    private String agreementInterestRate;
    private String agreementStatus;
    private String agreementSum;
}
