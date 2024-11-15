package de.yehorsh.customerservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.yehorsh.customerservice.CustomerServiceApplication;
import de.yehorsh.customerservice.config.ContainersEnvironment;
import de.yehorsh.customerservice.dto.CustomerCreateDto;
import de.yehorsh.customerservice.dto.CustomerDto;
import de.yehorsh.customerservice.dto.CustomerUpdateDto;
import de.yehorsh.customerservice.exception.CustomerNotFoundException;
import de.yehorsh.customerservice.model.Customer;
import de.yehorsh.customerservice.model.enums.CustomerStatus;
import de.yehorsh.customerservice.repository.CustomerRepository;
import de.yehorsh.customerservice.service.CustomerService;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(classes = CustomerServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {ContainersEnvironment.Initializer.class})
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DBUtil dbUtil;
    @Autowired
    private MeterRegistry meterRegistry;

    @BeforeEach
    void cleanUpDatabase() {
        customerRepository.deleteAll();
        dbUtil = new DBUtil(dataSource);
        meterRegistry.clear();
    }

    @ParameterizedTest
    @CsvSource({
            // Valid customer details with all valid fields
            "'Clark', 'Kent', 'clark.kent@example.com', '+1234567890', '12345678', '123 Main St', 'Metropolis', '54321', " +
                    "'USA', 201, 'Customer was successfully created'",
            // First name is empty
            "'', 'Doe', 'john.doe@example.com', '+123456789', '12345678', '123 Main St', 'Metropolis', '54321', 'USA', 400, ''",
            // Last name is empty
            "'John', '', 'john.doe@example.com', '+123456789', '12345678', '123 Main St', 'Metropolis', '54321', 'USA', 400, ''",
            // Email is empty
            "'John', 'Doe', '', '+123456789', '12345678', '123 Main St', 'Metropolis', '54321', 'USA', 400, ''",
            // Phone number is empty
            "'John', 'Doe', 'john.doe@example.com', '', '12345678', '123 Main St', 'Metropolis', '54321', 'USA', 400, ''",
            // Invalid email format
            "'John', 'Doe', 'invalid-email', '+123456789', '12345678', '123 Main St', 'Metropolis', '54321', 'USA', 400, ''",
            // Invalid phone number format
            "'John', 'Doe', 'john.doe@example.com', 'invalid-phone', '12345678', '123 Main St', 'Metropolis', '54321', 'USA', 400, ''",
            // Invalid tax number format
            "'John', 'Doe', 'john.doe@example.com', '+123456789', 'short', '123 Main St', 'Metropolis', '54321', 'USA', 400, ''",
            // Invalid address format
            "'John', 'Doe', 'john.doe@example.com', '+123456789', '12345678', 'St', 'Metropolis', '54321', 'USA', 400, ''",
            // Invalid city format
            "'John', 'Doe', 'john.doe@example.com', '+123456789', '12345678', '123 Main St', 'A', '54321', 'USA', 400, ''",
            // Invalid zip code format
            "'John', 'Doe', 'john.doe@example.com', '+123456789', '12345678', '123 Main St', 'Metropolis', '12', 'USA', 400, ''",
            // Invalid country format
            "'John', 'Doe', 'john.doe@example.com', '+123456789', '12345678', '123 Main St', 'Metropolis', '54321', 'A', 400, ''"
    })
    void test_createCustomer_invalidCases(String firstName, String lastName, String email, String phoneNumber,
                                          String taxNumber, String address, String city, String zipCode,
                                          String country, int expectedStatus, String expectedResponse) throws Exception {
        createCustomerAndExpect(firstName, lastName, email, phoneNumber, taxNumber, address, city, zipCode, country, expectedStatus, expectedResponse);
    }

    @Test
    void test_createCustomer_success() throws Exception {
        // prepare
        var requestCounter = meterRegistry.counter("create_customer_endpoint_count");
        double initialCount = customerRepository.count();

        var requestTimer = meterRegistry.timer("create_customer_endpoint_timer");
        long initialTimerCount = customerRepository.count();

        createCustomerAndExpect(
                "Clark",
                "Kent",
                "clark.kent@example.com",
                "+1234567890",
                "123456789",
                "123 Main Street",
                "Metropolis",
                "54321",
                "USA",
                201,
                "Customer was successfully created"
        );

        assertThat(dbUtil.customerExistsByEmail("clark.kent@example.com")).isTrue();

        // test
        Customer createCustomer = customerRepository.findCustomerByEmail("clark.kent@example.com").get();

        // assert
        assertThat(createCustomer.getFirstName()).isEqualTo("Clark");
        assertThat(createCustomer.getLastName()).isEqualTo("Kent");
        assertThat(createCustomer.getEmail()).isEqualTo("clark.kent@example.com");
        assertThat(createCustomer.getPhoneNumber()).isEqualTo("+1234567890");
        assertThat(createCustomer.getTaxNumber()).isEqualTo("123456789");
        assertThat(createCustomer.getAddress()).isEqualTo("123 Main Street");
        assertThat(createCustomer.getCity()).isEqualTo("Metropolis");
        assertThat(createCustomer.getZipCode()).isEqualTo("54321");
        assertThat(createCustomer.getCountry()).isEqualTo("USA");

        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
        assertThat(requestTimer.count()).isEqualTo(initialTimerCount + 1);
    }

    @Test
    void test_createCustomerWithExistingEmail_badRequest() throws Exception {
        createCustomerAndExpect(
                "Lois",
                "Kent",
                "lois.kent@example.com",
                "+1234567892",
                "987654321",
                "456 Elm Street",
                "Metropolis",
                "12345",
                "USA",
                201,
                "Customer was successfully created"
        );
        createCustomerAndExpect(
                "Lois",
                "Lane",
                "lois.kent@example.com",
                "+1987654321",
                "987654321",
                "456 Elm Street",
                "Metropolis",
                "12345",
                "USA",
                400,
                "{\"httpStatus\":" +
                        "\"BAD_REQUEST\",\"message\":" +
                        "\"Customer with the provided details already exists\"}"
        );
        assertThat(dbUtil.customerExistsByEmail("lois.kent@example.com")).isTrue();
    }

    @Test
    void test_createCustomerWithSamePhone_badRequest() throws Exception {
        createCustomerAndExpect(
                "Lois",
                "Kent",
                "lois.kent@example.com",
                "+1234567890",
                "987654321",
                "456 Elm Street",
                "Metropolis",
                "12345",
                "USA",
                201,
                "Customer was successfully created"
        );
        createCustomerAndExpect(
                "Lois",
                "Lane",
                "clark.kent@example.com",
                "+1234567890",
                "987654321",
                "456 Elm Street",
                "Metropolis",
                "12345",
                "USA",
                400,
                "{\"httpStatus\":" +
                        "\"BAD_REQUEST\",\"message\":" +
                        "\"Customer with the provided details already exists\"}"
        );
        assertThat(dbUtil.customerExistsByEmail("lois.kent@example.com")).isTrue();
    }

    @Test
    void test_createCustomerWithSameTaxNumber_badRequest() throws Exception {
        createCustomerAndExpect(
                "Lois",
                "Kent",
                "lois.kent@example.com",
                "+1234567890",
                "123456789",
                "456 Elm Street",
                "Metropolis",
                "12345",
                "USA",
                201,
                "Customer was successfully created"
        );
        createCustomerAndExpect(
                "Tony",
                "Stark",
                "tony.stark@example.com",
                "+1098765432",
                "123456789",
                "10880 Malibu Point",
                "Malibu",
                "90265",
                "USA",
                400,
                "{\"httpStatus\":" +
                        "\"BAD_REQUEST\",\"message\":" +
                        "\"Customer with the provided details already exists\"}"
        );
        assertThat(dbUtil.customerExistsByEmail("lois.kent@example.com")).isTrue();
    }

    @Test
    void test_findCustomer_success() throws Exception {
        // prepare
        Customer expectedCustomer = customerService.createNewCustomer(
                new CustomerCreateDto(
                        "Tony",
                        "Stark",
                        "tony.stark@example.com",
                        "+1098765432",
                        "123456789",
                        "10880 Malibu Point",
                        "Malibu",
                        "90265",
                        "USA"));

        var requestCounter = meterRegistry.counter("find_customer_endpoint_count");
        double initialCount = requestCounter.count();

        // test
        mockMvc.perform(MockMvcRequestBuilders.get("/customers/" + expectedCustomer.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // assert
        assertThat(dbUtil.customerExistsByEmail("tony.stark@example.com")).isTrue();
        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
    }

    @Test
    void test_findAllCustomer_success() throws Exception {
        // prepare
        Customer newCustomer1 = customerService.createNewCustomer(
                new CustomerCreateDto(
                        "Tony",
                        "Stark",
                        "tony.stark@example.com",
                        "+1098765432",
                        "123456789",
                        "10880 Malibu Point",
                        "Malibu",
                        "90265",
                        "USA"));
        Customer newCustomer2 = customerService.createNewCustomer(
                new CustomerCreateDto(
                        "Peter",
                        "Parker",
                        "peter.parker@example.com",
                        "+1234567890",
                        "564738292",
                        "15 Queens Blvd",
                        "New York",
                        "10001",
                        "USA"));

        var requestCounter = meterRegistry.counter("find_allCustomer_endpoint_count");
        double initialCount = requestCounter.count();

        // test
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<CustomerDto> customers = objectMapper.readValue(jsonResponse,
                new TypeReference<>() {
                });

        // assert
        assertThat(customers).hasSize(2);
        CustomerDto customer1 = customers.getFirst();
        assertThat(customer1.customerId()).isEqualTo(newCustomer1.getId());
        assertThat(customer1.firstName()).isEqualTo("Tony");
        assertThat(customer1.lastName()).isEqualTo("Stark");
        assertThat(customer1.email()).isEqualTo("tony.stark@example.com");
        assertThat(customer1.phoneNumber()).isEqualTo("+1098765432");
        assertThat(customer1.taxNumber()).isEqualTo("123456789");
        assertThat(customer1.address()).isEqualTo("10880 Malibu Point");
        assertThat(customer1.city()).isEqualTo("Malibu");
        assertThat(customer1.zipCode()).isEqualTo("90265");
        assertThat(customer1.country()).isEqualTo("USA");
        assertThat(customer1.createdAt()).isNotNull();
        assertThat(customer1.updatedAt()).isNotNull();
        assertThat(customer1.customerStatus()).isNotNull();

        CustomerDto customer2 = customers.get(1);
        assertThat(customer2.customerId()).isEqualTo(newCustomer2.getId());
        assertThat(customer2.firstName()).isEqualTo("Peter");
        assertThat(customer2.lastName()).isEqualTo("Parker");
        assertThat(customer2.email()).isEqualTo("peter.parker@example.com");
        assertThat(customer2.phoneNumber()).isEqualTo("+1234567890");
        assertThat(customer2.taxNumber()).isEqualTo("564738292");
        assertThat(customer2.address()).isEqualTo("15 Queens Blvd");
        assertThat(customer2.city()).isEqualTo("New York");
        assertThat(customer2.zipCode()).isEqualTo("10001");
        assertThat(customer2.country()).isEqualTo("USA");
        assertThat(customer2.createdAt()).isNotNull();
        assertThat(customer2.updatedAt()).isNotNull();
        assertThat(customer2.customerStatus()).isNotNull();

        assertThat(dbUtil.customerExistsByEmail("tony.stark@example.com")).isTrue();
        assertThat(dbUtil.customerExistsByEmail("peter.parker@example.com")).isTrue();
        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
    }

    @Test
    void test_updateCustomer_success() throws Exception {
        // prepare
        Customer expectedCustomer = customerService.createNewCustomer(
                new CustomerCreateDto(
                        "Tony",
                        "Stark",
                        "tony.stark@example.com",
                        "+1098765432",
                        "123456789",
                        "10880 Malibu Point",
                        "Malibu",
                        "90265",
                        "USA"));

        CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto(
                expectedCustomer.getId(),
                "Peter",
                "Parker",
                "peter.parker@example.com",
                "+1234567890",
                "1343414341",
                "15 Queens Blvd",
                "New York",
                "10001",
                "USA",
                CustomerStatus.ACTIVE);

        var requestCounter = meterRegistry.counter("update_customer_endpoint_count");
        double initialCount = requestCounter.count();

        String customerJson = objectMapper.writeValueAsString(customerUpdateDto);

        // test
        mockMvc.perform(MockMvcRequestBuilders.put("/customers/" + expectedCustomer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isOk());

        // assert
        assertThat(dbUtil.customerExistsByEmail("peter.parker@example.com")).isTrue();
        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);

        Customer actualCustomer = customerRepository.findById(expectedCustomer.getId()).get();

        assertThat(actualCustomer.getFirstName()).isEqualTo("Peter");
        assertThat(actualCustomer.getLastName()).isEqualTo("Parker");
        assertThat(actualCustomer.getEmail()).isEqualTo("peter.parker@example.com");
        assertThat(actualCustomer.getPhoneNumber()).isEqualTo("+1234567890");
        assertThat(actualCustomer.getAddress()).isEqualTo("15 Queens Blvd");
        assertThat(actualCustomer.getCity()).isEqualTo("New York");
        assertThat(actualCustomer.getZipCode()).isEqualTo("10001");
        assertThat(actualCustomer.getCountry()).isEqualTo("USA");
        assertThat(actualCustomer.getCustomerStatus()).isEqualTo(CustomerStatus.ACTIVE);
    }

    @Test
    void test_deleteCustomer_success() throws Exception {
        // prepare
        Customer expectedCustomer = customerService.createNewCustomer(
                new CustomerCreateDto(
                        "Tony",
                        "Stark",
                        "tony.stark@example.com",
                        "+1098765432",
                        "123456789",
                        "10880 Malibu Point",
                        "Malibu",
                        "90265",
                        "USA"));

        var requestCounter = meterRegistry.counter("delete_customer_endpoint_count");
        double initialCount = requestCounter.count();

        String customerJson = objectMapper.writeValueAsString(expectedCustomer);

        // test
        mockMvc.perform(MockMvcRequestBuilders.delete("/customers/" + expectedCustomer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer with ID " + expectedCustomer.getId() + " was deleted"));

        // assert
        assertThat(dbUtil.customerExistsByEmail("tony.stark@example.com")).isFalse();
        assertThat(customerRepository.existsById(expectedCustomer.getId()))
                .as("Customer should be deleted").isFalse();
        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
    }

    @Test
    void test_deleteNotExistingCustomer_badRequest() {
        UUID customerId = UUID.randomUUID();

        assertThat(customerRepository.existsById(customerId)).isFalse();

        assertThatThrownBy(() -> customerService.deleteCustomer(customerId))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("Customer with Id: " + customerId + " not found");

        // assert
        assertThat(customerRepository.existsById(customerId)).isFalse();
    }

    @Test
    void test_deleteCustomerWithNullId_badRequest() {
        assertThatThrownBy(() -> customerService.deleteCustomer(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Customer id cannot be null");
    }

    private void createCustomerAndExpect(String firstName, String lastName, String email, String phoneNumber,
                                         String taxNumber, String address, String city, String zipCode,
                                         String country, int expectedStatus, String expectedResponse) throws Exception {
        CustomerCreateDto newCustomerDto = new CustomerCreateDto(firstName, lastName, email, phoneNumber,
                taxNumber, address, city, zipCode, country);
        String customerJson = objectMapper.writeValueAsString(newCustomerDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().string(expectedResponse));
    }
}