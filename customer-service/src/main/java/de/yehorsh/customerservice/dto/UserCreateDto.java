package de.yehorsh.customerservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateDto(@NotBlank(message = "Email should not be empty")
                            @Email(message = "Email should be valid")
                            @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email address")
                            String email,

                            @NotBlank(message = "Password cannot be empty")
                            @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
                            @Pattern(regexp = "^(?=.*\\d)(?=.*\\p{Ll})(?=.*\\p{Lu})(?=.*\\p{Punct})[\\p{L}\\d\\p{Punct}]*$",
                                    message = "The password is incorrect. Password is required to contain "
                                            + "at least one uppercase and one lowercase, also one digit and one special character")
                            String password,

                            String roleName) {
}
