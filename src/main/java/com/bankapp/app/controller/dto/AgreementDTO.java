package com.bankapp.app.controller.dto;

import lombok.*;

@Data
public class AgreementDTO {
    /**
     * Agreement
     */

    private String agreementInterestRate;
    private String agreementStatus;
    private String agreementSum;

    /**
     * Product
     */

    private String productName;
    private String productStatus;
    private String currencyCode;
    private String productInterestRate;
    private String productLimit;

    /**
     * Account
     */

    private String accountName;
    private String accountType;
    private String accountStatus;
    private String accountBalance;

    /**
     * Client
     */
    private String clientStatus;
    private String clientLastName;
}
