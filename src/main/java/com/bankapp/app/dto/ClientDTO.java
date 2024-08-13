package com.bankapp.app.dto;

import lombok.*;

@Data
public class ClientDTO {
    private String status;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    private String taxCode;
}

