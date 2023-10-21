package com.bankapp.app.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AccountDTO {
    /**
     * Account
     */
    private String accountName;
    private String accountType;
    private String accountStatus;
    private String currencyCode;
    private String accountBalance;
    private String createdAt;

    /**
     * Client
     */
    private String clientStatus;
    private String clientLastName;
}
