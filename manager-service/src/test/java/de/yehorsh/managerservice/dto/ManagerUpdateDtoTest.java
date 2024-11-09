package de.yehorsh.managerservice.dto;
import de.yehorsh.managerservice.config.ValidatorTestBase;
import de.yehorsh.managerservice.model.enums.ManagerStatus;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ManagerUpdateDtoTest extends ValidatorTestBase {
    @Test
    void test_valid_managerUpdateDto_success() {
        ManagerUpdateDto dto = new ManagerUpdateDto(
                UUID.randomUUID(),
                "John",
                "Doe",
                "john.doe@example.com",
                "+123456789",
                ManagerStatus.ACTIVE
        );
        Set<ConstraintViolation<ManagerUpdateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertThat(violations).isEmpty();
    }

    @Test
    void test_invalid_firstNameWithInvalidCharacters_fail() {
        ManagerUpdateDto dto = new ManagerUpdateDto(
                UUID.randomUUID(),
                "John123",
                "Doe",
                "john.doe@example.com",
                "+123456789",
                ManagerStatus.ACTIVE
        );
        Set<ConstraintViolation<ManagerUpdateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .as("Invalid first name: only alphabetic characters are allowed")
                .isEqualTo("Invalid first name: only alphabetic characters are allowed");
    }

    @Test
    void test_invalid_firstNameBlank_fail() {
        ManagerUpdateDto dto = new ManagerUpdateDto(
                UUID.randomUUID(),
                "",
                "Doe",
                "john.doe@example.com",
                "+123456789",
                ManagerStatus.ACTIVE
        );
        Set<ConstraintViolation<ManagerUpdateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertThat(violations).hasSize(2);

        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        assertThat(messages)
                .as("Expected 'Invalid first name' message")
                .contains("Invalid first name: only alphabetic characters are allowed");
        assertThat(messages)
                .as("Can not be empty")
                .contains("Can not be empty");
    }

    @Test
    void test_invalid_lastNameWithInvalidCharacters_fail() {
        ManagerUpdateDto dto = new ManagerUpdateDto(
                UUID.randomUUID(),
                "John",
                "Doe123",
                "john.doe@example.com",
                "+123456789",
                ManagerStatus.ACTIVE
        );
        Set<ConstraintViolation<ManagerUpdateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .as("Invalid last name: only alphabetic characters are allowed")
                .isEqualTo("Invalid last name: only alphabetic characters are allowed");
    }

    @Test
    void test_invalid_lastNameBlank_fail() {
        ManagerUpdateDto dto = new ManagerUpdateDto(
                UUID.randomUUID(),
                "John",
                "",
                "john.doe@example.com",
                "+123456789",
                ManagerStatus.ACTIVE
        );
        Set<ConstraintViolation<ManagerUpdateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertThat(violations).hasSize(2);

        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        assertThat(messages)
                .as("Expected 'Invalid last name' message")
                .contains("Invalid last name: only alphabetic characters are allowed");
        assertThat(messages)
                .as("Can not be empty")
                .contains("Can not be empty");
    }

    @Test
    void test_invalid_emailWithInvalidCharacters_fail() {
        ManagerUpdateDto dto = new ManagerUpdateDto(
                UUID.randomUUID(),
                "John",
                "Doe",
                "invalid-email",
                "+123456789",
                ManagerStatus.ACTIVE
        );
        Set<ConstraintViolation<ManagerUpdateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertThat(violations).isNotEmpty();
        assertThat(violations).as("Expected 2 validation errors").hasSize(2);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Invalid email address");
    }

    @Test
    void test_invalid_emailBlank_fail() {
        ManagerUpdateDto dto = new ManagerUpdateDto(
                UUID.randomUUID(),
                "John",
                "Doe",
                "",
                "+123456789",
                ManagerStatus.ACTIVE
        );
        Set<ConstraintViolation<ManagerUpdateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertThat(violations).hasSize(2);

        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        assertThat(messages)
                .as("Expected 'Invalid email address.' message")
                .contains("Invalid email address");
        assertThat(messages)
                .as("Can not be empty")
                .contains("Can not be empty");
    }

    @Test
    void test_invalid_phoneNumberWithInvalidCharacters_fail() {
        ManagerUpdateDto dto = new ManagerUpdateDto(
                UUID.randomUUID(),
                "John",
                "Doe",
                "john.doe@example.com",
                "123",
                ManagerStatus.ACTIVE
        );
        Set<ConstraintViolation<ManagerUpdateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .as("Invalid phone number: it must be in a valid format " +
                        "(e.g., +123456789, (123) 456-7890, 123-456-7890)")
                .isEqualTo("Invalid phone number: it must be in a valid format " +
                        "(e.g., +123456789, (123) 456-7890, 123-456-7890)");
    }

    @Test
    void test_invalidMultipleWithInvalidCharacters_fail() {
        ManagerUpdateDto dto = new ManagerUpdateDto(
                UUID.randomUUID(),
                "John123",
                "Doe456",
                "invalid-email",
                "123",
                ManagerStatus.ACTIVE
        );
        Set<ConstraintViolation<ManagerUpdateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertThat(violations).as("Expected 5 validation errors").hasSize(5);

        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        assertThat(messages)
                .as("Expected 'Invalid first name' message")
                .contains("Invalid first name: only alphabetic characters are allowed");
        assertThat(messages)
                .as("Expected 'Invalid last name' message")
                .contains("Invalid last name: only alphabetic characters are allowed");
        assertThat(messages)
                .as("Expected 'Invalid phone number' message")
                .contains("Invalid phone number: it must be in a valid format " +
                        "(e.g., +123456789, (123) 456-7890, 123-456-7890)");
        assertThat(messages)
                .as("Expected 'Invalid email address.' message")
                .contains("Invalid email address");
    }

    private void printViolations(Set<ConstraintViolation<ManagerUpdateDto>> violations) {
        violations.forEach(violation ->
                System.out.println(violation.getPropertyPath() + " " + violation.getMessage()));
    }
}
