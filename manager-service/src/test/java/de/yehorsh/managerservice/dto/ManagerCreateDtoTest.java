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
            // Testing: Empty first name, valid last name, email, and phone number
            "'', 'Doe', 'john.doe@example.com', '+123456789', 2, 'First name should not be empty'",

            // Testing: Valid first name, empty last name, valid email, and phone number
            "'John', '', 'john.doe@example.com', '+123456789', 2, 'Last name should not be empty'",

            // Testing: Valid first name, last name, and phone number, but invalid email format
            "'John', 'Doe', 'invalid-email', '+123456789', 2, 'Email should be valid'",

            // Testing: Valid first name, last name, and email, but invalid phone number format
            "'John', 'Doe', 'john.doe@example.com', 'invalid-phone', 1, 'Phone number should be valid'"
    })
    void test_invalidManagerCreateDto(String firstName, String lastName, String email, String phone,
                                      int expectedViolationCount, String errorMessage) {

        ManagerCreateDto dto = new ManagerCreateDto(firstName, lastName, email, phone);
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
                "+123456789");
        Set<ConstraintViolation<ManagerCreateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(0, violations.size(), "Should be valid");
    }

    @ParameterizedTest
    @CsvSource({
            // Testing: Valid first name and last name, but invalid email (single '@' symbol)
            "'John', 'Doe', '@', '+123456789', 2, 'Email should be valid'",

            // Testing: Valid first name and last name, but empty email
            "'John', 'Doe', '', '+123456789', 2, 'Email should not be empty'",

            // Testing: Valid first name and last name, but email with multiple '@' symbols
            "'John', 'Doe', 'invalid@email@.gmail.com', '+123456789', 2, 'Email should be valid'",

            // Testing: Valid first name and last name, but email with only a '.' (invalid)
            "'John', 'Doe', '.', '+123456789', 2, 'Email should be valid'"
    })
    void test_invalidEmail(String firstName, String lastName, String email, String phone, int expectedViolationCount, String errorMessage) {
        ManagerCreateDto dto = new ManagerCreateDto(firstName, lastName, email, phone);
        Set<ConstraintViolation<ManagerCreateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    @ParameterizedTest
    @CsvSource({
            // Testing: Valid first name, last name, and email, but invalid phone (too short)
            "'John', 'Doe', 'john.doe@example.com', '123', 1, 'Phone number should be valid'",

            // Testing: Valid first name, last name, and email, but invalid phone (too long)
            "'John', 'Doe', 'john.doe@example.com', '+12313123131231319028', 1, 'Phone number should be valid'"
    })
    void test_invalidPhoneNumber(String firstName, String lastName, String email, String phone,
                                 int expectedViolationCount, String errorMessage) {
        ManagerCreateDto dto = new ManagerCreateDto(firstName, lastName, email, phone);
        Set<ConstraintViolation<ManagerCreateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    @ParameterizedTest
    @CsvSource({
            // Testing: First name contains numeric characters
            "'John123', 'Doe', 'john.doe@example.com', '+123456789', 1, " +
                    "'First name should only contain alphabetic characters'",

            // Testing: Last name contains numeric characters
            "'John', 'Doe123', 'john.doe@example.com', '+123456789', 1, " +
                    "'Last name should only contain alphabetic characters'"
    })
    void test_invalidNameFields(String firstName, String lastName, String email, String phone,
                                int expectedViolationCount, String errorMessage) {
        ManagerCreateDto dto = new ManagerCreateDto(firstName, lastName, email, phone);
        Set<ConstraintViolation<ManagerCreateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    private void printViolations(Set<ConstraintViolation<ManagerCreateDto>> violations) {
        violations.forEach(violation ->
                System.out.println(violation.getPropertyPath() + " " + violation.getMessage()));
    }
}
