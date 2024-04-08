package com.bankapp.app.controller.dto;

import lombok.Data;

@Data
public class ClientDTOFull {
    private String clientStatus;
    private String clientFirstName;
    private String clientLastName;
    private String taxCode;
    private String email;
    private String address;
    private String phoneNumber;
    private String manager;
}
