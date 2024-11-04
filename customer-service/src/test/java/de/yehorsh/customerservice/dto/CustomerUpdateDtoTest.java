package de.yehorsh.customerservice.dto;

import de.yehorsh.customerservice.config.ValidatorTestBase;
import de.yehorsh.customerservice.model.enums.CustomerStatus;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerUpdateDtoTest extends ValidatorTestBase {

    @ParameterizedTest
    @CsvSource({
            // Empty phone number
            "'John', 'Doe', 'john.doe@example.com', '', 'validTax123', '123 Main St', 'City', '12345', 'Country', " +
                    "'ACTIVE', 1, 'Invalid phone number format, use one of: +123456789, (123) 456-7890, 123-456-7890'",

            // Empty first name
            "'', 'Doe', 'john.doe@example.com', '+123456789', 'validTax123', '123 Main St', 'City', '12345', 'Country', " +
                    "'ACTIVE', 2, 'First name must not be empty; Invalid first name: only alphabetic characters are allowed'",

            // Invalid first name (contains numbers)
            "'John123', 'Doe', 'john.doe@example.com', '+123456789', 'validTax123', '123 Main St', 'City', '12345', " +
                    "'Country', 'ACTIVE', 1, 'Invalid first name: only alphabetic characters are allowed'",

            // Empty last name
            "'John', '', 'john.doe@example.com', '+123456789', 'validTax123', '123 Main St', 'City', '12345', 'Country', " +
                    "'ACTIVE', 2, 'Last name must not be empty; Invalid last name: only alphabetic characters are allowed'",

            // Invalid email (missing '@')
            "'John', 'Doe', 'invalid-email', '+123456789', 'validTax123', '123 Main St', 'City', '12345', 'Country', " +
                    "'ACTIVE', 2, 'Invalid email address.; Invalid email address.'",

            // Invalid taxNumber (less than 8 characters)
            "'John', 'Doe', 'john.doe@example.com', '+123456789', 'short', '123 Main St', 'City', '12345', 'Country', " +
                    "'ACTIVE', 1, 'Invalid tax number'",

            // Valid data
            "'John', 'Doe', 'john.doe@example.com', '+123456789', 'validTax123', '123 Main St', 'City', '12345', " +
                    "'Country', 'ACTIVE', 0, 'Should be valid'"
    })
    void test_invalidCustomerUpdateDto(String firstName, String lastName, String taxNumber, String email, String phoneNumber,
                                       String address, String city, String zipCode, String country, String customerStatus,
                                       int expectedViolationCount, String errorMessage) {
        UUID id = UUID.randomUUID();
        CustomerStatus status = CustomerStatus.valueOf(customerStatus);

        CustomerUpdateDto dto = new CustomerUpdateDto(id, firstName, lastName, taxNumber, email, phoneNumber, address, city, zipCode, country, status);
        Set<ConstraintViolation<CustomerUpdateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    @Test
    void test_validCustomerUpdateDto() {
        UUID id = UUID.randomUUID();
        CustomerUpdateDto dto = new CustomerUpdateDto(
                id,
                "John",
                "Doe",
                "john.doe@example.com",
                "+123456789",
                "A12345678",
                "123 Main St",
                "City",
                "12345",
                "Country",
                CustomerStatus.ACTIVE);

        Set<ConstraintViolation<CustomerUpdateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(0, violations.size(), "Should be valid");
    }

    private void printViolations(Set<ConstraintViolation<CustomerUpdateDto>> violations) {
        violations.forEach(violation ->
                System.out.println(violation.getPropertyPath() + " " + violation.getMessage()));
    }
}
