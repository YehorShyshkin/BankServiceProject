package com.bankapp.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CardDTO {

    /**
     * Card
     */

    private String cardNumber;
    private String holderName;
    private String cardBalance;
    private String cardPaymentSystem;
    private String cardStatus;

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

}
