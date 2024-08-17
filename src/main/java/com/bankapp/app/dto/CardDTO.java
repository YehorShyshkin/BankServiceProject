package com.bankapp.app.dto;

import lombok.Data;


@Data
public class CardDTO {
    private String number;
    private String holder;
    private String expirationDate;
    private String cvv;
    private String paymentSystem;
    private String status;
    private String cardType;
    private String clientId;
    private String accountId;
}



