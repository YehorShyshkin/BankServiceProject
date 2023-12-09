package com.bankapp.app.dto;

import lombok.Data;


@Data
public class CardDTO {

    /**
     * Card
     */

    private String cardNumber;
    private String cardBalance;
    private String cardPaymentSystem;
    private String cardStatus;

    /**
     * Account
     */
//       private UUID accountId;
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



