package com.bankapp.app.controller.dto;

import lombok.*;

@Data
public class ClientDTO {
    /**
     * Client
     */

    private String clientStatus;
    private String clientFirstName;
    private String clientLastName;
    private String email;
    private String address;
    private String phoneNumber;
    private String taxCode;

}

