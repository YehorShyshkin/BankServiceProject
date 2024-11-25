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
                "ValidUser",
                "ValidPassword1!",
                "valid.email@example.com",
                "USER");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(0, violations.size(), "Should be valid");
    }

    @ParameterizedTest
    @CsvSource({
            // Empty username, valid password, email, and role (testing the `@NotBlank` constraint)
            "'', 'ValidPassword1!', 'valid.email@example.com', 'USER', 3, 'Username cannot be empty, Username must be between 4 and 20 characters, Username can only contain letters, numbers, underscores, and hyphens'",

            // Username too short (less than 4 characters)
            "'abc', 'ValidPassword1!', 'valid.email@example.com', 'USER', 1, 'Username must be between 4 and 20 characters'",

            // Invalid username characters (contains a space, which is invalid)
            "'Invalid User', 'ValidPassword1!', 'valid.email@example.com', 'USER', 1, 'Username can only contain letters, numbers, underscores, and hyphens'"
    })
    void test_invalidUserName(String userName, String password, String email, String roleName, int expectedViolationCount, String errorMessage) {
        UserDto dto = new UserDto(userName, password, email, roleName);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    @ParameterizedTest
    @CsvSource({
            // Empty password, valid username, email, and role
            "'ValidUser', '', 'valid.email@example.com', 'USER', 3, 'Password cannot be empty'",

            // Password too short (less than 8 characters)
            "'ValidUser', 'short1!', 'valid.email@example.com', 'USER', 2, 'Password must be between 8 and 64 characters'",

            // Password without an uppercase letter
            "'ValidUser', 'validpassword1', 'valid.email@example.com', 'USER', 1, 'The password is incorrect. Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'",

            // Password without a digit
            "'ValidUser', 'ValidPassword!', 'valid.email@example.com', 'USER', 1, 'The password is incorrect. Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'",

            // Password without a special character
            "'ValidUser', 'Valid1234', 'valid.email@example.com', 'USER', 1, 'The password is incorrect. Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'"
    })
    void test_invalidPassword(String userName, String password, String email, String roleName, int expectedViolationCount, String errorMessage) {
        UserDto dto = new UserDto(userName, password, email, roleName);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    @ParameterizedTest
    @CsvSource({
            // Empty email, valid username, password, and role
            "'ValidUser', 'ValidPassword1!', '', 'USER', 2, 'Email should not be empty, Invalid email address'",

            // Invalid email format (missing '@')
            "'ValidUser', 'ValidPassword1!', 'invalid-email', 'USER', 2, 'Invalid email address'",

            // Invalid email format (too many '@' symbols)
            "'ValidUser', 'ValidPassword1!', 'invalid@email@domain.com', 'USER', 2, 'Invalid email address'",

            // Invalid email format (missing domain)
            "'ValidUser', 'ValidPassword1!', 'invalid@.com', 'USER', 2, 'Invalid email address'",

            // Invalid email format (only '.' without domain)
            "'ValidUser', 'ValidPassword1!', '.', 'USER', 2, 'Invalid email address'"
    })
    void test_invalidEmail(String userName, String password, String email, String roleName, int expectedViolationCount, String errorMessage) {
        UserDto dto = new UserDto(userName, password, email, roleName);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    private void printViolations(Set<ConstraintViolation<UserDto>> violations) {
        violations.forEach(violation ->
                System.out.println(violation.getPropertyPath() + " " + violation.getMessage()));
    }
}