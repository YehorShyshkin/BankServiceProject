package com.bankapp.app.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
public class AccountDTO {
    private String name;
    private String type;
    private String status;
    private String currencyCode;
    private BigDecimal balance;

    /**
     * Client
     */

    private String clientId;

    /**
     * Agreement
     */

    private String agreementId;
}
