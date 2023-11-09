package com.bankapp.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CardStatusUpdateDTO {
    private String cardNumber;
    private String cardBalance;
    private String cardPaymentSystem;
    private String cardStatus;
}
