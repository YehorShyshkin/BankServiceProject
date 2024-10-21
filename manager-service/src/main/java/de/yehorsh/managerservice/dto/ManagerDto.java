package de.yehorsh.managerservice.dto;

import de.yehorsh.managerservice.model.Manager;
import de.yehorsh.managerservice.model.enums.ManagerStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ManagerDto(UUID managerId,
                         @Pattern(regexp = "^\\p{L}+$",
                                 message = "Invalid first name: only alphabetic characters are allowed!")
                         String firstName,
                         @Pattern(regexp = "^\\p{L}+$",
                                 message = "Invalid last name: only alphabetic characters are allowed!")
                         String lastName,
                         @Email
                         @Pattern(
                                 regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                                 message = "Invalid email address"
                         )
                         String email,
                         @Pattern(regexp = "^\\+?[0-9\\s().-]{7,15}$",
                                 message = "Invalid phone number: it must be in a valid format " +
                                         "(e.g., +123456789, (123) 456-7890, 123-456-7890)")
                         String phoneNumber,
                         ManagerStatus managerStatus,
                         OffsetDateTime creationDate,
                         OffsetDateTime updateDate) {

    public static ManagerDto fromManager(Manager manager) {
        return new ManagerDto(manager.getId(), manager.getFirstName(), manager.getLastName(),
                manager.getEmail(), manager.getPhoneNumber(), manager.getManagerStatus(),
                manager.getCreationDate(), manager.getUpdateDate());
    }
}

