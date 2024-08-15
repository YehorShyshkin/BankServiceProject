package com.bankapp.app.dto;

import com.bankapp.app.generator.AllowedDomains;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Data
public class ClientDTO {

    private String status;
    @NotBlank(message = "Can not be empty")
    @Pattern(regexp = "^\\p{L}+$", message = "First name must contain only letters")
    private String firstName;
    @NotBlank(message = "Can not be empty")
    @Pattern(regexp = "^\\p{L}+$", message = "Last name must contain only letters")
    private String lastName;
    @Email
    @AllowedDomains
    private String email;
    @Size(max = 100,
            message = "Address must be at most 100 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9 ,\\.]+$",
            message = "Address can only contain letters, numbers, spaces, commas, and periods")
    private String address;

    @Size(min = 10, max = 15,
            message = "Phone number must be between 10 and 15 characters long")
    @Pattern(regexp = "^[0-9+\\-]*$",
            message = "Phone number can only contain digits, plus and hyphen")
    private String phoneNumber;

    @NotBlank(message = "Tax code cannot be blank")
    @Pattern(regexp = "^\\d{10}$",
            message = "Tax code must be exactly 10 digits")
    private String taxCode;

    /**
     * Manager
     */
    private UUID managerId;
}

