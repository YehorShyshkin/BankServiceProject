package com.bankapp.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AgreementDTO {
    /**
     * Agreement
     */

    private String agreementInterestRate;
    private String agreementStatus;
    private String agreementSum;
    private String createdAt;

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
