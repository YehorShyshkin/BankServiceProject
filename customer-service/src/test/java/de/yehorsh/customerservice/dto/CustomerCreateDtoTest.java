package de.yehorsh.customerservice.dto;

import de.yehorsh.customerservice.config.ValidatorTestBase;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerCreateDtoTest extends ValidatorTestBase {

    @ParameterizedTest
    @CsvSource({
            // Empty email
            "'John', 'Doe', '', '+123456789', '123456789', '123 Main St', 'City', '12345', 'Country', 2, " +
                    "'Email must not be empty'",

            // Invalid email formats
            "'John', 'Doe', 'invalid-email', '+123456789', '123456789', '123 Main St', 'City', '12345', 'Country', 2, " +
                    "'Invalid email address'",
            "'John', 'Doe', 'user@', '+123456789', '123456789', '123 Main St', 'City', '12345', 'Country', 2, " +
                    "'Invalid email address'",
            "'John', 'Doe', '@domain.com', '+123456789', '123456789', '123 Main St', 'City', '12345', 'Country', 2, " +
                    "'Invalid email address'",
            "'John', 'Doe', 'user@.com', '+123456789', '123456789', '123 Main St', 'City', '12345', 'Country', 2, " +
                    "'Invalid email address'",

            // Empty phone
            "'John', 'Doe', 'john.doe@example.com', '', '123456789', '123 Main St', 'City', '12345', 'Country', 1, " +
                    "'Invalid phone number: it must be in a valid format (e.g., +123456789, (123) 456-7890, 123-456-7890)'"
    })
    void test_invalidCustomerCreateDto(String firstName, String lastName, String email, String phoneNumber,
                                       String taxNumber, String address, String city, String zipCode,
                                       String country, int expectedViolationCount, String errorMessage) {

        CustomerCreateDto dto = new CustomerCreateDto(firstName, lastName, email, phoneNumber, taxNumber, address, city, zipCode, country);
        Set<ConstraintViolation<CustomerCreateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    @ParameterizedTest
    @CsvSource({
            // Invalid first name (contains numeric characters)
            "'John123', 'Doe', 'john.doe@example.com', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 1, 'Invalid first name: only alphabetic characters are allowed'",

            // Invalid last name (contains numeric characters)
            "'John', 'Doe123', 'john.doe@example.com', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 1, 'Invalid last name: only alphabetic characters are allowed'"
    })
    void test_invalidNameFields(String firstName, String lastName, String email, String phoneNumber,
                                String taxNumber, String address, String city, String zipCode,
                                String country, int expectedViolationCount, String errorMessage) {
        CustomerCreateDto dto = new CustomerCreateDto(firstName, lastName, email, phoneNumber, taxNumber, address, city, zipCode, country);
        Set<ConstraintViolation<CustomerCreateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    @ParameterizedTest
    @CsvSource({
            // Invalid email (no '@' symbol)
            "'John', 'Doe', 'invalidemail.com', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 2, 'Invalid email address'",

            // Invalid email with spaces
            "'John', 'Doe', 'invalid email@domain.com', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 2, 'Invalid email address'",

            // Invalid email with special characters
            "'John', 'Doe', 'invalid!email@domain.com', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 1, 'Invalid email address'",

            // Excessively long email
            "'John', 'Doe', 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@domain.com', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 2, 'Email is too long'",

            // Invalid domain structure
            "'John', 'Doe', 'user@domain..com', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 1, 'Invalid email address'"
    })
    void test_invalidEmail(String firstName, String lastName, String email, String phoneNumber,
                           String taxNumber, String address, String city, String zipCode,
                           String country, int expectedViolationCount, String errorMessage) {
        CustomerCreateDto dto = new CustomerCreateDto(firstName, lastName, email, phoneNumber, taxNumber, address, city, zipCode, country);
        Set<ConstraintViolation<CustomerCreateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    @ParameterizedTest
    @CsvSource({
            // Too short phone
            "'John', 'Doe', 'john.doe@example.com', '123', '12345678', '123 Main St', 'City', '12345', 'Country', 1, " +
                    "'Invalid phone number format, use one of: +123456789, (123) 456-7890, 123-456-7890'",

            // Too long phone
            "'John', 'Doe', 'john.doe@example.com', '+1234567890123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 1, " +
                    "'Invalid phone number format, use one of: +123456789, (123) 456-7890, 123-456-7890'",

            // Without plus
            "'John', 'Doe', 'john.doe@example.com', '1234567890123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 1, " +
                    "'Invalid phone number format, use one of: +123456789, (123) 456-7890, 123-456-7890'",

            // With letters
            "'John', 'Doe', 'john.doe@example.com', '+12345abc890', '12345678', '123 Main St', 'City', '12345', 'Country', 1, " +
                    "'Invalid phone number format, use one of: +123456789, (123) 456-7890, 123-456-7890'"
    })
    void test_invalidPhoneNumber(String firstName, String lastName, String email, String phoneNumber,
                                 String taxNumber, String address, String city, String zipCode,
                                 String country, int expectedViolationCount, String errorMessage) {
        CustomerCreateDto dto = new CustomerCreateDto(firstName, lastName, email, phoneNumber, taxNumber, address, city, zipCode, country);
        Set<ConstraintViolation<CustomerCreateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    @Test
    void test_validCustomerCreateDto() {
        CustomerCreateDto dto = new CustomerCreateDto(
                "John",
                "Doe",
                "john.doe@example.com",
                "+123456789",
                "12345678",
                "123 Main St",
                "City",
                "12345",
                "Country");
        Set<ConstraintViolation<CustomerCreateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(0, violations.size(), "Should be valid");
    }

    private void printViolations(Set<ConstraintViolation<CustomerCreateDto>> violations) {
        violations.forEach(violation ->
                System.out.println(violation.getPropertyPath() + " " + violation.getMessage()));
    }
}
