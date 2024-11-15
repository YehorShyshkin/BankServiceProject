package de.yehorsh.customerservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerCreateDto(
        @NotBlank(message = "Can not be empty")
        @Pattern(regexp = "^\\p{L}+$", message = "Invalid first name: only alphabetic characters are allowed")
        String firstName,

        @NotBlank(message = "Can not be empty")
        @Pattern(regexp = "^\\p{L}+$", message = "Invalid last name: only alphabetic characters are allowed")
        String lastName,

        @NotBlank(message = "Email cannot be empty.")
        @Email(message = "Invalid email address")
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email address"
        )
        @Size(max = 254, message = "Email is too long")
        String email,

        // format +1234567890, (123) 456-7890, 123-456-7890
        @Pattern(regexp = "^\\+?\\d{1,3}\\s?\\(?\\d{1,4}\\)?[-\\s]?\\d{1,4}[-\\s]?\\d{1,4}$",
                message = "Invalid phone number format, use one of: +123456789, (123) 456-7890, 123-456-7890")
        String phoneNumber,

        @Pattern(regexp = "^[A-Za-z0-9]{8,15}$", message = "Invalid tax number")
        String taxNumber,

        // a-z A-Z 0-9 space . , ' - length:5-100
        @Pattern(regexp = "^[\\w\\s.,'-]{5,100}$",
                message = "Invalid address")
        String address,

        // a-z A-Z space ' , - length:2-50
        @Pattern(regexp = "^[A-Za-z\\s'-]{2,50}$", message = "Invalid city")
        String city,

        // a-z A-Z 0-9 space - length:3-10
        @Pattern(regexp = "^[A-Za-z0-9\\s-]{3,10}$", message = "Invalid zip code")
        String zipCode,

        // a-z A-Z space ' , - length:2-50
        @Pattern(regexp = "^[A-Za-z\\s'-]{2,50}$", message = "Invalid country")
        String country) {
}