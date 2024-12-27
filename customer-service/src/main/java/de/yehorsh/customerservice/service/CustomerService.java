package de.yehorsh.customerservice.service;

import de.yehorsh.customerservice.aspect.LogInfo;
import de.yehorsh.customerservice.dto.CustomerCreateDto;
import de.yehorsh.customerservice.dto.CustomerDto;
import de.yehorsh.customerservice.dto.CustomerUpdateDto;
import de.yehorsh.customerservice.dto.UserCreateDto;
import de.yehorsh.customerservice.exception.AuthServiceException;
import de.yehorsh.customerservice.exception.CustomerNotFoundException;
import de.yehorsh.customerservice.exception.DuplicateFieldException;
import de.yehorsh.customerservice.model.Customer;
import de.yehorsh.customerservice.model.enums.CustomerStatus;
import de.yehorsh.customerservice.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;

    @LogInfo(name = "create_customer_service")
    public Customer createNewCustomer(CustomerCreateDto customerCreateDto) {
        if (customerRepository.existsByEmailOrPhoneNumberOrTaxNumber(
                customerCreateDto.email(),
                customerCreateDto.phoneNumber(),
                customerCreateDto.taxNumber())) {
            log.warn("Customer with email: {}, phoneNumber: {}, or taxNumber: {} already exists",
                    customerCreateDto.email(),
                    customerCreateDto.phoneNumber(),
                    customerCreateDto.taxNumber());
            throw new IllegalArgumentException("Customer with the provided details already exists");
        }

        UserCreateDto userDto = new UserCreateDto(
                customerCreateDto.email(),
                customerCreateDto.password(),
                "CUSTOMER"
        );

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                "/users/create", userDto, String.class);

        if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
            log.error("Failed to create user in AuthService: {}", responseEntity.getStatusCode());
            throw new AuthServiceException("Failed to create user in AuthService");
        }

        String userId = responseEntity.getBody();
        if (userId == null || userId.isEmpty()) {
            log.error("AuthService returned an empty userId");
            throw new AuthServiceException("Failed to retrieve userId from AuthService");
        }

        Customer createNewCustomer = Customer.builder()
                .firstName(customerCreateDto.firstName())
                .lastName(customerCreateDto.lastName())
                .email(customerCreateDto.email())
                .taxNumber(customerCreateDto.taxNumber())
                .phoneNumber(customerCreateDto.phoneNumber())
                .city(customerCreateDto.city())
                .address(customerCreateDto.address())
                .zipCode(customerCreateDto.zipCode())
                .country(customerCreateDto.country())
                .customerStatus(CustomerStatus.ACTIVE)
                .userId(userId)
                .build();

        Customer savedCustomer = customerRepository.save(createNewCustomer);
        log.info("Successfully created new customer: {}", savedCustomer);

        return savedCustomer;
    }

    @LogInfo(name = "find_customer_by_id_service")
    public Customer findCustomerById(UUID customerId) {
        log.debug("Attempting to find customer by id: {}", customerId);
        return customerRepository.findById(customerId)
                .map(customer -> {
                    log.info("Successfully found customer with Id: {}", customerId);
                    return customer;
                })
                .orElseThrow(() -> {
                    log.error("Customer with id {} not found", customerId);
                    return new CustomerNotFoundException("Customer with Id: " + customerId + " not found");
                });
    }

    @LogInfo(name = "find_allCustomer_service")
    public List<CustomerDto> findAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        List<CustomerDto> list = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerDto customerDto = CustomerDto.fromCustomer(customer);
            list.add(customerDto);
        }
        return list;
    }

    @LogInfo(name = "update_customer_service")
    public void updateCustomer(UUID customerId, CustomerUpdateDto customerUpdateDto) {
        Customer customer = findCustomerById(customerId);
        log.debug("Found customer for update with Id: {}", customerId);

        if (customerRepository.existsByEmail(customerUpdateDto.email()) &&
                !customerUpdateDto.email().equals(customer.getEmail())) {
            log.warn("Email {} is already taken by another customer", customerUpdateDto.email());
            throw new DuplicateFieldException("This email is already registered to another account");
        }

        if (!customerUpdateDto.phoneNumber().equals(customer.getPhoneNumber()) &&
                customerRepository.existsByPhoneNumber(customerUpdateDto.phoneNumber())) {
            log.warn("Phone number {} is already taken by another customer", customerUpdateDto.phoneNumber());
            throw new DuplicateFieldException("This phone number is already registered to another account");
        }

        if (!customerUpdateDto.taxNumber().equals(customer.getTaxNumber()) &&
                customerRepository.existsByTaxNumber(customerUpdateDto.taxNumber())) {
            log.warn("Tax number {} is already taken by another customer", customerUpdateDto.taxNumber());
            throw new DuplicateFieldException("This tax number is already registered to another account");
        }

        if (customerRepository.existsByTaxNumber(customerUpdateDto.taxNumber()) &&
                !customer.getTaxNumber().equals(customerUpdateDto.taxNumber())) {
            log.warn("Tax number {} is already taken by another customer", customerUpdateDto.taxNumber());
            throw new DuplicateFieldException("This tax number is already registered to another account");
        }

        customer.setFirstName(customerUpdateDto.firstName());
        customer.setLastName(customerUpdateDto.lastName());
        customer.setEmail(customerUpdateDto.email());
        customer.setPhoneNumber(customerUpdateDto.phoneNumber());
        customer.setTaxNumber(customerUpdateDto.taxNumber());
        customer.setAddress(customerUpdateDto.address());
        customer.setCity(customerUpdateDto.city());
        customer.setZipCode(customerUpdateDto.zipCode());
        customer.setCountry(customerUpdateDto.country());
        customer.setCustomerStatus(customerUpdateDto.customerStatus());

        customerRepository.save(customer);
        log.info("Successfully updated customer with Id: {}", customerId);
    }

    @LogInfo(name = "delete_customer_service")
    public void deleteCustomer(UUID customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer id cannot be null");
        }

        Customer customer = findCustomerById(customerId);
        log.debug("Found customer for deletion: {}", customer);

        if (customer == null) {
            log.warn("Attempted to delete a customer with Id: {} that does not exist", customerId);
            throw new CustomerNotFoundException("Customer with Id: " + customerId + " not found");
        }

        customerRepository.delete(customer);
        log.info("Successfully deleted customer with Id: {}", customerId);
    }
}
