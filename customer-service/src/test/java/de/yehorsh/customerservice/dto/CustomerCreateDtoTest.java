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
            "'John', 'Doe', '', 'Password1!', '+123456789', '123456789', '123 Main St', 'City', '12345', 'Country', 2, " +
                    "'Email cannot be empty'",

            // Invalid email formats
            "'John', 'Doe', 'invalid-email', 'Password1!', '+123456789', '123456789', '123 Main St', 'City', '12345', 'Country', 2, " +
                    "'Invalid email address'",

            // Empty password
            "'John', 'Doe', 'john.doe@example.com', '', '+123456789', '123456789', '123 Main St', 'City', '12345', 'Country', 2, " +
                    "'Password must be between 8 and 64 characters, The password is incorrect. Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'",

            // Password too short
            "'John', 'Doe', 'john.doe@example.com', 'Short1!', '+123456789', '123456789', '123 Main St', 'City', '12345', 'Country', 1, " +
                    "'Password must be between 8 and 64 characters'"
    })
    void test_invalidCustomerCreateDto(String firstName, String lastName, String email, String password, String phoneNumber,
                                       String taxNumber, String address, String city, String zipCode,
                                       String country, int expectedViolationCount, String errorMessage) {

        CustomerCreateDto dto = new CustomerCreateDto(firstName, lastName, email, password, phoneNumber, taxNumber, address, city, zipCode, country);
        Set<ConstraintViolation<CustomerCreateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    @ParameterizedTest
    @CsvSource({
            // Invalid first name (contains numeric characters)
            "'John123', 'Doe', 'john.doe@example.com', 'Password1!', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 1, 'Invalid first name: only alphabetic characters are allowed'",

            // Invalid last name (contains numeric characters)
            "'John', 'Doe123', 'john.doe@example.com', 'Password1!', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 1, 'Invalid last name: only alphabetic characters are allowed'"
    })
    void test_invalidNameFields(String firstName, String lastName, String email, String password, String phoneNumber,
                                String taxNumber, String address, String city, String zipCode,
                                String country, int expectedViolationCount, String errorMessage) {
        CustomerCreateDto dto = new CustomerCreateDto(firstName, lastName, email, password, phoneNumber, taxNumber, address, city, zipCode, country);
        Set<ConstraintViolation<CustomerCreateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    @ParameterizedTest
    @CsvSource({
            // Invalid email (no '@' symbol)
            "'John', 'Doe', 'invalidemail.com', 'Password1!', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 2, 'Invalid email address'",

            // Invalid email with spaces
            "'John', 'Doe', 'invalid email@domain.com', 'Password1!', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 2, 'Invalid email address'",

            // Invalid email with special characters
            "'John', 'Doe', 'invalid!email@domain.com', 'Password1!', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 1, 'Invalid email address'",

            // Excessively long email
            "'John', 'Doe', 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@domain.com', 'Password1!', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 2, 'Email is too long'",

            // Invalid domain structure
            "'John', 'Doe', 'user@domain..com', 'Password1!', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 1, 'Invalid email address'"
    })
    void test_invalidEmail(String firstName, String lastName, String email, String password, String phoneNumber,
                           String taxNumber, String address, String city, String zipCode,
                           String country, int expectedViolationCount, String errorMessage) {
        CustomerCreateDto dto = new CustomerCreateDto(firstName, lastName, email, password, phoneNumber, taxNumber, address, city, zipCode, country);
        Set<ConstraintViolation<CustomerCreateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    @ParameterizedTest
    @CsvSource({
            // Too short phone
            "'John', 'Doe', 'john.doe@example.com', 'Password1!', '123', '12345678', '123 Main St', 'City', '12345', 'Country', 1, " +
                    "'Invalid phone number format, use one of: +123456789, (123) 456-7890, 123-456-7890'",

            // Too long phone
            "'John', 'Doe', 'john.doe@example.com', 'Password1!', '+1234567890123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 1, " +
                    "'Invalid phone number format, use one of: +123456789, (123) 456-7890, 123-456-7890'",

            // Without plus
            "'John', 'Doe', 'john.doe@example.com', 'Password1!', '1234567890123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 1, " +
                    "'Invalid phone number format, use one of: +123456789, (123) 456-7890, 123-456-7890'",

            // With letters
            "'John', 'Doe', 'john.doe@example.com', 'Password1!', '+12345abc890', '12345678', '123 Main St', 'City', '12345', 'Country', 1, " +
                    "'Invalid phone number format, use one of: +123456789, (123) 456-7890, 123-456-7890'"
    })
    void test_invalidPhoneNumber(String firstName, String lastName, String email, String password, String phoneNumber,
                                 String taxNumber, String address, String city, String zipCode,
                                 String country, int expectedViolationCount, String errorMessage) {
        CustomerCreateDto dto = new CustomerCreateDto(firstName, lastName, email, password, phoneNumber, taxNumber, address, city, zipCode, country);
        Set<ConstraintViolation<CustomerCreateDto>> violations = validator.validate(dto);

        printViolations(violations);

        assertEquals(expectedViolationCount, violations.size(), errorMessage);
    }

    @ParameterizedTest
    @CsvSource({
            // Password too short (less than 8 characters)
            "'John', 'Doe', 'john.doe@example.com', 'Short1!', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 1, 'Password must be between 8 and 64 characters'",

            // Password too long (more than 64 characters)
            "'John', 'Doe', 'john.doe@example.com', 'AVeryLongPassword1234567890ThatExceedsTheMaxLengthOf64Characters!!', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 1, 'Password must be between 8 and 64 characters'",

            // Password without any digits
            "'John', 'Doe', 'john.doe@example.com', 'PasswordWithoutDigits!', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 1, 'The password is incorrect. Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'",

            // Password without any uppercase letter
            "'John', 'Doe', 'john.doe@example.com', 'passwordwithoutuppercase1!', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 1, 'The password is incorrect. Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'",

            // Password without any special character
            "'John', 'Doe', 'john.doe@example.com', 'PasswordWithoutSpecial1', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 1, 'The password is incorrect. Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'",

            // Password with valid format
            "'John', 'Doe', 'john.doe@example.com', 'ValidPassword1!', '+123456789', '12345678', '123 Main St', 'City', '12345', 'Country', 0, 'No error, password is valid'"
    })
    void test_invalidPassword(String firstName, String lastName, String email, String password, String phoneNumber,
                              String taxNumber, String address, String city, String zipCode,
                              String country, int expectedViolationCount, String errorMessage) {
        CustomerCreateDto dto = new CustomerCreateDto(firstName, lastName, email, password, phoneNumber, taxNumber, address, city, zipCode, country);

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
                "StrongP@ssw0rd!",
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
