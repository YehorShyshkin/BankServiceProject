package de.yehorsh.managerservice.dto;

import de.yehorsh.managerservice.config.ValidatorTestBase;
import de.yehorsh.managerservice.model.Manager;
import de.yehorsh.managerservice.model.enums.ManagerStatus;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ManagerDtoTest extends ValidatorTestBase {
    @Test
    void test_validManagerDto_success() {
        UUID managerId = UUID.randomUUID();
        Manager manager = new Manager(
                managerId,
                "John",
                "Doe",
                ManagerStatus.ACTIVE,
                "john.doe@example.com",
                "+123456789",
                "e0ae9b88-8306-4b16-91f8-13c4adb6bada",
                OffsetDateTime.now(),
                OffsetDateTime.now());

        ManagerDto managerDto = ManagerDto.fromManager(manager);

        assertThat(managerDto.managerId()).isEqualTo(managerId);
        assertThat(managerDto.firstName()).isEqualTo("John");
        assertThat(managerDto.lastName()).isEqualTo("Doe");
        assertThat(managerDto.email()).isEqualTo("john.doe@example.com");
        assertThat(managerDto.phoneNumber()).isEqualTo("+123456789");
        assertThat(managerDto.managerStatus()).isEqualTo(ManagerStatus.ACTIVE);
    }

    @Test
    void test_validationManagerDto() {
        ManagerDto validDto = new ManagerDto(
                UUID.randomUUID(),
                "Jane",
                "Doe",
                "jane.doe@example.com",
                "+123456789",
                ManagerStatus.ACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now());

        Set<ConstraintViolation<ManagerDto>> violations = validator.validate(validDto);

        printViolations(violations);

        assertThat(violations).as("Validation should pass for valid ManagerDto").isEmpty();
    }

    @ParameterizedTest
    @CsvSource({
            // Testing invalid first name (contains numbers)
            "John1, Doe, john.doe@example.com, +123456789",
            // Testing invalid last name (contains numbers)
            "John, Doe2, john.doe@example.com, +123456789",
            // Testing invalid email (incorrect email format)
            "John, Doe, invalidEmail, +123456789",
            // Testing invalid phone number (incorrect phone format)
            "John, Doe, john.doe@example.com, invalidPhone"
    })
    void test_invalidFields(String firstName, String lastName, String email, String phoneNumber) {
        ManagerDto invalidDto = new ManagerDto(
                UUID.randomUUID(),
                firstName,
                lastName,
                email,
                phoneNumber,
                ManagerStatus.ACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now());

        Set<ConstraintViolation<ManagerDto>> violations = validator.validate(invalidDto);

        printViolations(violations);

        assertThat(violations).as("Validation should pass for valid ManagerDto").isNotEmpty();
    }

    private void printViolations(Set<ConstraintViolation<ManagerDto>> violations) {
        violations.forEach(violation ->
                System.out.println(violation.getPropertyPath() + " " + violation.getMessage()));
    }
}
