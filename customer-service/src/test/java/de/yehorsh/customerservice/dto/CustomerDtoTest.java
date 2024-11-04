package de.yehorsh.customerservice.dto;

import de.yehorsh.customerservice.config.ValidatorTestBase;
import de.yehorsh.customerservice.model.Customer;
import de.yehorsh.customerservice.model.enums.CustomerStatus;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CustomerDtoTest extends ValidatorTestBase {

    @ParameterizedTest
    @CsvSource({
            // Invalid first name (contains numbers)
            "John1, Doe, john.doe@example.com, +123456789, 123456789, 123 Main St, City, 12345, Country",
            // Invalid last name (contains numbers)
            "John, Doe2, john.doe@example.com, +123456789, 123456789, 123 Main St, City, 12345, Country",
            // Invalid email (incorrect email format)
            "John, Doe, invalidEmail, +123456789, 123456789, 123 Main St, City, 12345, Country",
            // Invalid phone number (incorrect format)
            "John, Doe, john.doe@example.com, invalidPhone, 123456789, 123 Main St, City, 12345, Country",
            // Invalid tax number (too short)
            "John, Doe, john.doe@example.com, +123456789, 123, 123 Main St, City, 12345, Country"
    })
    void test_invalidCustomerDto(
            String firstName, String lastName, String email, String phoneNumber,
            String taxNumber, String address, String city, String zipCode, String country) {

        CustomerDto invalidDto = new CustomerDto(
                UUID.randomUUID(),
                firstName,
                lastName,
                email,
                phoneNumber,
                taxNumber,
                address,
                city,
                zipCode,
                country,
                CustomerStatus.ACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now());

        Set<ConstraintViolation<CustomerDto>> violations = validator.validate(invalidDto);

        printViolations(violations);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void test_validCustomerDto_success() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(
                customerId,
                "John",
                "Doe",
                "john.doe@example.com",
                "+123456789",
                "123456789",
                "123 Main St",
                "City",
                "12345",
                "Country",
                CustomerStatus.ACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now());

        CustomerDto customerDto = CustomerDto.fromCustomer(customer);

        assertThat(customerDto.customerId()).isEqualTo(customerId);
        assertThat(customerDto.firstName()).isEqualTo("John");
        assertThat(customerDto.lastName()).isEqualTo("Doe");
        assertThat(customerDto.email()).isEqualTo("john.doe@example.com");
        assertThat(customerDto.phoneNumber()).isEqualTo("+123456789");
        assertThat(customerDto.customerStatus()).isEqualTo(CustomerStatus.ACTIVE);
        assertThat(customerDto.address()).isEqualTo("123 Main St");
        assertThat(customerDto.city()).isEqualTo("City");
        assertThat(customerDto.zipCode()).isEqualTo("12345");
        assertThat(customerDto.country()).isEqualTo("Country");
    }

    @Test
    void test_validationValidCustomerDto() {
        CustomerDto validDto = new CustomerDto(
                UUID.randomUUID(),
                "Jane",
                "Doe",
                "jane.doe@example.com",
                "+123456789",
                "123456789",
                "123 Main St",
                "City",
                "12345",
                "Country",
                CustomerStatus.ACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now());

        Set<ConstraintViolation<CustomerDto>> violations = validator.validate(validDto);

        printViolations(violations);

        assertThat(violations).isEmpty();
    }

    private void printViolations(Set<ConstraintViolation<CustomerDto>> violations) {
        violations.forEach(violation ->
                System.out.println(violation.getPropertyPath() + " " + violation.getMessage()));
    }
}
