package com.bankapp.app.dto;

import lombok.*;

@Data
public class AccountDTO {
    private String name;
    private String type;
    private String status;
    private String currencyCode;
    private String balance;
}
