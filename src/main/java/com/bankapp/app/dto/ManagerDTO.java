package com.bankapp.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ManagerDTO {
    @NotBlank(message = "Can not be empty")
    @Pattern(regexp = "^\\p{L}+$", message = "First name must contain only letters")
    private String firstName;
    @NotBlank(message = "Can not be empty")
    @Pattern(regexp = "^\\p{L}+$", message = "Last name must contain only letters")
    private String lastName;
    private String status;
}

