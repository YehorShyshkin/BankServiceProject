package de.yehorsh.managerservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ManagerCreateDto(@NotBlank(message = "Can not be empty.")
                               @Pattern(regexp = "^\\p{L}+$",
                                       message = "Invalid first name: only alphabetic characters are allowed!")
                               String firstName,
                               @NotBlank(message = "Can not be empty.")
                               @Pattern(regexp = "^\\p{L}+$",
                                       message = "Invalid last name: only alphabetic characters are allowed!")
                               String lastName,
                               @NotBlank(message = "Email should not be empty.")
                               @Email(message = "Email should be valid")
                               @Pattern(
                                       regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                                       message = "Invalid email address"
                               )
                               String email,
                               @Pattern(regexp = "^\\+?[0-9\\s().-]{7,15}$",
                                       message = "Invalid phone number: it must be in a valid format " +
                                               "(e.g., +123456789, (123) 456-7890, 123-456-7890)")
                               String phoneNumber) {
}
