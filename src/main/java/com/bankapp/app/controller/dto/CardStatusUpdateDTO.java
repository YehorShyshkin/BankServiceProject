package com.bankapp.app.controller.dto;

import lombok.Data;

@Data
public class CardStatusUpdateDTO {
    private String cardNumber;
    private String cardBalance;
    private String cardPaymentSystem;
    private String cardStatus;
}
