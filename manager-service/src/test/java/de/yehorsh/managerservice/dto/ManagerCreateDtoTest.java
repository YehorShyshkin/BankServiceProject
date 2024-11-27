package de.yehorsh.managerservice.dto;

import de.yehorsh.managerservice.config.ValidatorTestBase;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManagerCreateDtoTest extends ValidatorTestBase {

    @ParameterizedTest
    @CsvSource({
            // Empty first name, valid last name, email, and phone number, invalid password
            "'', 'Doe', 'john.doe@example.com', '+123456789', 'short', 4, 'First name should not be empty, " +
                    "Password should be at least 8 characters long, Password should contain at least one uppercase and one lowercase letter, " +
                    "Password should contain at least one special character'",

            // Valid first name, empty last name, valid email, and phone number, invalid password
            "'John', '', 'john.doe@example.com', '+123456789', 'short', 4, 'Last name should not be empty, " +
                    "Password must be between 8 and 64 characters, The password is incorrect. " +
                    "Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'",

            // Valid first name, last name, and phone number, but invalid email format, invalid password
            "'John', 'Doe', 'invalid-email', '+123456789', 'short', 4, 'Email should be valid, Password should be at least 8 characters long, " +
                    "Password should contain at least one uppercase and one lowercase letter, " +
                    "Password should contain at least one special character'",

            // Valid first name, last name, and email, but invalid phone number format, invalid password
            "'John', 'Doe', 'john.doe@example.com', 'invalid-phone', 'short', 3, 'Invalid phone number: " +
                    "it must be in a valid format (e.g., +123456789, (123) 456-7890, 123-456-7890), " +
                    "Password should be at least 8 characters long, Password should contain at least one uppercase and one lowercase letter'"
    })
    void test_invalidManagerCreateDto(String firstName, String lastName, String email, String phone, String password,
                                      int expectedViolationCount, String errorMessage) {
        ManagerCreateDto dto = new ManagerCreateDto(firstName, lastName, email, phone, password);
        Set<ConstraintViolation<ManagerCreateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    @Test
    void test_validManagerCreateDto() {
        ManagerCreateDto dto = new ManagerCreateDto(
                "John",
                "Doe",
                "john.doe@example.com",
                "+123456789",
                "Password1!");
        Set<ConstraintViolation<ManagerCreateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(0, violations.size(), "Should be valid");
    }

    @ParameterizedTest
    @CsvSource({
            // Valid first name and last name, but invalid email (single '@' symbol), invalid password
            "'John', 'Doe', '@', '+123456789', 'Password1!', 2, 'Email should be valid'",

            // Valid first name and last name, but empty email, invalid password
            "'John', 'Doe', '', '+123456789', 'Password1!', 2, 'Email should not be empty'",

            // Valid first name and last name, but email with multiple '@' symbols, invalid password
            "'John', 'Doe', 'invalid@email@.gmail.com', '+123456789', 'Password1!', 2, 'Email should be valid'",

            // Valid first name and last name, but email with only a '.' (invalid), invalid password
            "'John', 'Doe', '.', '+123456789', 'Password1!', 2, 'Email should be valid'"
    })
    void test_invalidEmail(String firstName, String lastName, String email, String phone, String password,
                           int expectedViolationCount, String errorMessage) {
        ManagerCreateDto dto = new ManagerCreateDto(firstName, lastName, email, phone, password);
        Set<ConstraintViolation<ManagerCreateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    @ParameterizedTest
    @CsvSource({
            // Valid first name, last name, and email, but invalid phone (too short), invalid password
            "'John', 'Doe', 'john.doe@example.com', '123', 'Password1!', 1, 'Phone number should be valid'",

            // Valid first name, last name, and email, but invalid phone (too long), invalid password
            "'John', 'Doe', 'john.doe@example.com', '+12313123131231319028', 'Password1!', 1, 'Phone number should be valid'"
    })
    void test_invalidPhoneNumber(String firstName, String lastName, String email, String phone, String password,
                                 int expectedViolationCount, String errorMessage) {
        ManagerCreateDto dto = new ManagerCreateDto(firstName, lastName, email, phone, password);
        Set<ConstraintViolation<ManagerCreateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    @ParameterizedTest
    @CsvSource({
            // First name contains numeric characters, invalid password
            "'John123', 'Doe', 'john.doe@example.com', '+123456789', 'Password1!', 1, " +
                    "'First name should only contain alphabetic characters'",

            // Last name contains numeric characters, invalid password
            "'John', 'Doe123', 'john.doe@example.com', '+123456789', 'Password1!', 1, " +
                    "'Last name should only contain alphabetic characters'"
    })
    void test_invalidNameFields(String firstName, String lastName, String email, String phone, String password,
                                int expectedViolationCount, String errorMessage) {
        ManagerCreateDto dto = new ManagerCreateDto(firstName, lastName, email, phone, password);
        Set<ConstraintViolation<ManagerCreateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    private void printViolations(Set<ConstraintViolation<ManagerCreateDto>> violations) {
        violations.forEach(violation ->
                System.out.println(violation.getPropertyPath() + " " + violation.getMessage()));
    }
}

