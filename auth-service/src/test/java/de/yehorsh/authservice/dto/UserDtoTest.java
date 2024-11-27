package de.yehorsh.authservice.dto;

import de.yehorsh.authservice.config.ValidatorTestBase;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDtoTest extends ValidatorTestBase {

    @Test
    void test_validUserDto() {
        UserDto dto = new UserDto(
                "valid.email@example.com",
                "ValidPassword1!",
                "MANAGER");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(0, violations.size(), "Should be valid");
    }

    @ParameterizedTest
    @CsvSource({
            // Empty password, valid username, email, and role
            "'valid.email@example.com', '', 'MANAGER', 3, 'Password cannot be empty'",

            // Password too short (less than 8 characters)
            "'valid.email@example.com', 'short1!', 'MANAGER', 2, 'Password must be between 8 and 64 characters'",

            // Password without an uppercase letter
            "'valid.email@example.com', 'validpassword1', 'MANAGER', 1, 'The password is incorrect. Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'",

            // Password without a digit
            "'valid.email@example.com', 'ValidPassword!', 'MANAGER', 1, 'The password is incorrect. Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'",

            // Password without a special character
            "'valid.email@example.com', 'Valid1234', 'MANAGER', 1, 'The password is incorrect. Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'"
    })
    void test_invalidPassword(String email, String password, String roleName, int expectedViolationCount, String errorMessage) {
        UserDto dto = new UserDto(email, password, roleName);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    @ParameterizedTest
    @CsvSource({
            // Empty email, valid username, password, and role
            "'','ValidPassword1!', 'USER', 2, 'Email should not be empty, Invalid email address'",

            // Invalid email format (missing '@')
            "'invalid-email', 'ValidPassword1!',  'MANAGER', 2, 'Invalid email address'",

            // Invalid email format (too many '@' symbols)
            "'invalid@email@domain.com','ValidPassword1!',  'MANAGER', 2, 'Invalid email address'",

            // Invalid email format (missing domain)
            "'invalid@.com', 'ValidPassword1!', 'CUSTOMER', 2, 'Invalid email address'",

            // Invalid email format (only '.' without domain)
            "'.', 'ValidPassword1!',  'CUSTOMER', 2, 'Invalid email address'"
    })
    void test_invalidEmail(String email, String password, String roleName, int expectedViolationCount, String errorMessage) {
        UserDto dto = new UserDto(email, password, roleName);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    private void printViolations(Set<ConstraintViolation<UserDto>> violations) {
        violations.forEach(violation ->
                System.out.println(violation.getPropertyPath() + " " + violation.getMessage()));
    }
}