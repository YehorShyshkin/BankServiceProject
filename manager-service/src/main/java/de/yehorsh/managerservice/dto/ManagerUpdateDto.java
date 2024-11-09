package de.yehorsh.managerservice.dto;

import de.yehorsh.managerservice.model.enums.ManagerStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record ManagerUpdateDto(UUID id,
                               @NotBlank(message = "Can not be empty")
                               @Pattern(regexp = "^\\p{L}+$",
                                       message = "Invalid first name: only alphabetic characters are allowed")
                               String firstName,
                               @NotBlank(message = "Can not be empty")
                               @Pattern(regexp = "^\\p{L}+$",
                                       message = "Invalid last name: only alphabetic characters are allowed")
                               String lastName,
                               @NotBlank(message = "Can not be empty")
                               @Email(message = "Invalid email address")
                               @Pattern(
                                       regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                                       message = "Invalid email address"
                               )
                               String email,
                               @Pattern(regexp = "^\\+?[0-9\\s().-]{7,15}$",
                                       message = "Invalid phone number: it must be in a valid format (e.g., +123456789, (123) 456-7890, 123-456-7890)")
                               String phoneNumber,
                               ManagerStatus managerStatus) {
}

