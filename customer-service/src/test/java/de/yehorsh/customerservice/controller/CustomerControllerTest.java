package de.yehorsh.customerservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import de.yehorsh.customerservice.CustomerServiceApplication;
import de.yehorsh.customerservice.config.ContainersEnvironment;
import de.yehorsh.customerservice.dto.CustomerCreateDto;
import de.yehorsh.customerservice.dto.CustomerDto;
import de.yehorsh.customerservice.dto.CustomerUpdateDto;
import de.yehorsh.customerservice.dto.UserCreateDto;
import de.yehorsh.customerservice.exception.CustomerNotFoundException;
import de.yehorsh.customerservice.model.Customer;
import de.yehorsh.customerservice.model.enums.CustomerStatus;
import de.yehorsh.customerservice.repository.CustomerRepository;
import de.yehorsh.customerservice.service.CustomerService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(classes = CustomerServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWireMock
@ContextConfiguration(initializers = {ContainersEnvironment.Initializer.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
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
    private static WireMockServer wireMockServer;
    private static String managerToken;
    private static String adminToken;
    private static String customerToken;

    @BeforeAll
    static void start() {
        wireMockServer = new WireMockServer(8084);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8084);

        long now = System.currentTimeMillis();
        String secretKey = "d91fb2a36c4ad89da9f322bcfcd7b357294eb77e8c44d5b36695f449ddacc76f";

        managerToken = Jwts.builder()
                .setSubject("manager@example.com")
                .claim("role", "MANAGER")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 3600000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        adminToken = Jwts.builder()
                .setSubject("admin@example.com")
                .claim("role", "ADMIN")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 3600000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        customerToken = Jwts.builder()
                .setSubject("customer@example.com")
                .claim("role", "CUSTOMER")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 3600000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/users/create"))
                .willReturn(WireMock.aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": \"12345\"}")));

        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/auth/login"))
                .withRequestBody(equalToJson("{\"email\": \"manager@example.com\", \"password\": \"StrongManagerP@ssw0rd!\"}"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"token\": \"" + managerToken + "\"}")));

        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/auth/login"))
                .withRequestBody(equalToJson("{\"email\": \"admin@example.com\", \"password\": \"StrongAdminP@ssw0rd!\"}"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"token\": \"" + adminToken + "\"}")));

        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/auth/login"))
                .withRequestBody(equalToJson("{\"email\": \"customer@example.com\", \"password\": \"StrongCustomerP@ssw0rd!\"}"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"token\": \"" + customerToken + "\"}")));
    }

    @AfterAll
    static void cleanUpWireMockServer()
    {
        wireMockServer.stop();
    }

    @BeforeEach
    void cleanUpDatabase()
    {
        customerRepository.deleteAll();
        dbUtil = new DBUtil(dataSource);
        meterRegistry.clear();
    }

    @ParameterizedTest
    @CsvSource({
            // Valid customer details with all valid fields
            "'Clark', 'Kent', 'clark.kent@example.com', 'StrongPassword1!', '+1234567890', '12345678', '123 Main St', 'Metropolis', '54321', 'USA', 201, 'Customer was successfully created'",

            // First name is empty
            "'', 'Doe', 'john.doe@example.com', 'StrongPassword1!', '+123456789', '12345678', '123 Main St', 'Metropolis', '54321', 'USA', 400, 'Can not be empty; Invalid first name: only alphabetic characters are allowed'",

            // Last name is empty
            "'John', '', 'john.doe@example.com', 'StrongPassword1!', '+123456789', '12345678', '123 Main St', 'Metropolis', '54321', 'USA', 400, 'Can not be empty; Invalid last name: only alphabetic characters are allowed'",

            // Email is empty
            "'John', 'Doe', '', 'StrongPassword1!', '+123456789', '12345678', '123 Main St', 'Metropolis', '54321', 'USA', 400, 'Email cannot be empty.; Invalid email address'",

            // Phone number is empty
            "'John', 'Doe', 'john.doe@example.com', 'StrongPassword1!', '', '12345678', '123 Main St', 'Metropolis', '54321', 'USA', 400, 'Invalid phone number format, use one of: +123456789, (123) 456-7890, 123-456-7890; Phone number cannot be empty'",

            // Invalid email format
            "'John', 'Doe', 'invalid-email', 'StrongPassword1!', '+123456789', '12345678', '123 Main St', 'Metropolis', '54321', 'USA', 400, 'Invalid email address; Invalid email address'",

            // Invalid phone number format
            "'John', 'Doe', 'john.doe@example.com', 'StrongPassword1!', 'invalid-phone', '12345678', '123 Main St', 'Metropolis', '54321', 'USA', 400, 'Invalid phone number format, use one of: +123456789, (123) 456-7890, 123-456-7890'",

            // Invalid tax number format
            "'John', 'Doe', 'john.doe@example.com', 'StrongPassword1!', '+123456789', 'short', '123 Main St', 'Metropolis', '54321', 'USA', 400, 'Invalid tax number'",

            // Invalid address format
            "'John', 'Doe', 'john.doe@example.com', 'StrongPassword1!', '+123456789', '12345678', 'St', 'Metropolis', '54321', 'USA', 400, 'Invalid address'",

            // Invalid city format
            "'John', 'Doe', 'john.doe@example.com', 'StrongPassword1!', '+123456789', '12345678', '123 Main St', 'A', '54321', 'USA', 400, 'Invalid city'",

            // Invalid zip code format
            "'John', 'Doe', 'john.doe@example.com', 'StrongPassword1!', '+123456789', '12345678', '123 Main St', 'Metropolis', '12', 'USA', 400, 'Invalid zip code'",

            // Invalid country format
            "'John', 'Doe', 'john.doe@example.com', 'StrongPassword1!', '+123456789', '12345678', '123 Main St', 'Metropolis', '54321', 'A', 400, 'Invalid country'"
    })
    void test_createCustomer_invalidCases(String firstName, String lastName, String email, String password, String phoneNumber,
                                          String taxNumber, String address, String city, String zipCode,
                                          String country, int expectedStatus, String expectedResponse) throws Exception {
        createCustomerAndExpect(firstName, lastName, email, password, phoneNumber, taxNumber, address, city,
                zipCode, country, expectedStatus, expectedResponse);
    }

    @Test
    void test_createCustomer_success() throws Exception {
        // prepare
        var requestCounter = meterRegistry.counter("create_customer_endpoint_count");
        double initialCount = customerRepository.count();

        var requestTimer = meterRegistry.timer("create_customer_endpoint_timer");
        long initialTimerCount = customerRepository.count();

        CustomerCreateDto customerCreateDto = new CustomerCreateDto(
                "Clark",
                "Kent",
                "clark.kent@example.com",
                "StrongP@ssw0rd!",
                "+1234567890",
                "123456789",
                "123 Main Street",
                "Metropolis",
                "54321",
                "USA"
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .header("Authorization", "Bearer " + managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerCreateDto)))
                .andExpect(status().isCreated())
                .andReturn();

        UserCreateDto expectedUserDto = new UserCreateDto(
                customerCreateDto.email(),
                "StrongP@ssw0rd!",
                "CUSTOMER"
        );

        wireMockServer.verify(postRequestedFor(urlEqualTo("/users/create"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(expectedUserDto))));

        assertThat(dbUtil.customerExistsByEmail("clark.kent@example.com")).isTrue();

        Customer createCustomer = customerRepository.findCustomerByEmail("clark.kent@example.com").orElseThrow();

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
        assertThat(createCustomer.getId()).isNotNull();

        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
        assertThat(requestTimer.count()).isEqualTo(initialTimerCount + 1);
    }

    @Test
    void test_createCustomerWithExistingEmail_badRequest() throws Exception {
        // prepare
        CustomerCreateDto firstCustomerCreateDto = new CustomerCreateDto(
                "Lois",
                "Kent",
                "lois.kent@example.com",
                "StrongP@ssw0rd!",
                "+1234567892",
                "987654321",
                "456 Elm Street",
                "Metropolis",
                "12345",
                "USA"
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .header("Authorization", "Bearer " + managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstCustomerCreateDto)))
                .andExpect(status().isCreated())
                .andReturn();

        CustomerCreateDto secondCustomerCreateDto = new CustomerCreateDto(
                "Diana",
                "Prince",
                "lois.kent@example.com",
                "Amaz0nWarri0r!",
                "+19998887766",
                "1122334455",
                "101 Paradise Island",
                "Themyscira",
                "54321",
                "Greece"
        );

        // test
        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .header("Authorization", "Bearer " + managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondCustomerCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"httpStatus\":" +
                        "\"BAD_REQUEST\",\"message\":" +
                        "\"Customer with the provided details already exists\"}"));

        assertThat(dbUtil.customerExistsByEmail("lois.kent@example.com")).isTrue();
    }

    @Test
    void test_createCustomerWithSamePhone_badRequest() throws Exception {
        // prepare
        CustomerCreateDto firstCustomerCreateDto = new CustomerCreateDto(
                "Lois",
                "Kent",
                "lois.kent@example.com",
                "StrongP@ssw0rd!",
                "+1234567892",
                "987654321",
                "456 Elm Street",
                "Metropolis",
                "12345",
                "USA"
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .header("Authorization", "Bearer " + managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstCustomerCreateDto)))
                .andExpect(status().isCreated())
                .andReturn();

        CustomerCreateDto secondCustomerCreateDto = new CustomerCreateDto(
                "Diana",
                "Prince",
                "diana.prince@example.com",
                "Amaz0nWarri0r!",
                "+1234567892",
                "1122334455",
                "101 Paradise Island",
                "Themyscira",
                "54321",
                "Greece"
        );

        // test
        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .header("Authorization", "Bearer " + managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondCustomerCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"httpStatus\":" +
                        "\"BAD_REQUEST\",\"message\":" +
                        "\"Customer with the provided details already exists\"}"));

        assertThat(dbUtil.customerExistsByEmail("lois.kent@example.com")).isTrue();
    }

    @Test
    void test_createCustomerWithSameTaxNumber_badRequest() throws Exception {
        // prepare
        CustomerCreateDto firstCustomerCreateDto = new CustomerCreateDto(
                "Lois",
                "Kent",
                "lois.kent@example.com",
                "StrongP@ssw0rd!",
                "+1234567892",
                "987654321",
                "456 Elm Street",
                "Metropolis",
                "12345",
                "USA"
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .header("Authorization", "Bearer " + managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstCustomerCreateDto)))
                .andExpect(status().isCreated())
                .andReturn();

        CustomerCreateDto secondCustomerCreateDto = new CustomerCreateDto(
                "Diana",
                "Prince",
                "diana.prince@example.com",
                "Amaz0nWarri0r!",
                "+19998887766",
                "987654321",
                "101 Paradise Island",
                "Themyscira",
                "54321",
                "Greece"
        );

        // test
        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .header("Authorization", "Bearer " + managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondCustomerCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"httpStatus\":" +
                        "\"BAD_REQUEST\",\"message\":" +
                        "\"Customer with the provided details already exists\"}"));

        assertThat(dbUtil.customerExistsByEmail("lois.kent@example.com")).isTrue();
    }

    @Test
    void test_findCustomer_success() throws Exception {
        // prepare
        CustomerCreateDto expectedCustomer = new CustomerCreateDto(
                "Lois",
                "Kent",
                "lois.kent@example.com",
                "StrongP@ssw0rd!",
                "+1234567892",
                "987654321",
                "456 Elm Street",
                "Metropolis",
                "12345",
                "USA"
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .header("Authorization", "Bearer " + managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedCustomer)))
                .andExpect(status().isCreated())
                .andReturn();

        var requestCounter = meterRegistry.counter("find_customer_endpoint_count");
        double initialCount = requestCounter.count();

        Customer createCustomer = customerRepository.findCustomerByEmail("lois.kent@example.com")
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        // test
        mockMvc.perform(MockMvcRequestBuilders.get("/customers/" + createCustomer.getId())
                        .header("Authorization", "Bearer " + managerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // assert
        assertThat(dbUtil.customerExistsByEmail("lois.kent@example.com")).isTrue();
        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
    }

    @Test
    void test_findAllCustomer_success() throws Exception {
        // prepare
        CustomerCreateDto newCustomer1 = new CustomerCreateDto(
                        "Tony",
                        "Stark",
                        "tony.stark@example.com",
                        "Password123!",
                        "+1098765432",
                        "123456789",
                        "10880 Malibu Point",
                        "Malibu",
                        "90265",
                        "USA");

        CustomerCreateDto newCustomer2 = new CustomerCreateDto(
                        "Peter",
                        "Parker",
                        "peter.parker@example.com",
                        "Password!2",
                        "+1234567890",
                        "564738292",
                        "15 Queens Blvd",
                        "New York",
                        "10001",
                        "USA");

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .header("Authorization", "Bearer " + managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCustomer1)))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .header("Authorization", "Bearer " + managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCustomer2)))
                .andExpect(status().isCreated());

        var requestCounter = meterRegistry.counter("find_allCustomer_endpoint_count");
        double initialCount = requestCounter.count();

        // test
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/customers/findAllCustomers")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<CustomerDto> customers = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        // assert
        assertThat(customers).hasSize(2);
        CustomerDto customer1 = customers.getFirst();
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
        CustomerCreateDto expectedCustomer = new CustomerCreateDto(
                        "Tony",
                        "Stark",
                        "tony.stark@example.com",
                        "StrongCustomerP@ssw0rd!",
                        "+1098765432",
                        "123456789",
                        "10880 Malibu Point",
                        "Malibu",
                        "90265",
                        "USA");

       mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .header("Authorization", "Bearer " + managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedCustomer)))
                .andExpect(status().isCreated())
                .andReturn();

        Customer updatedCustomer = customerRepository.findCustomerByEmail("tony.stark@example.com")
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto(
                updatedCustomer.getId(),
                "Tony",
                "Stark",
                "tony.stark@example.com",
                "+1234567890",
                "234567891",
                "15 Queens Blvd",
                "New York",
                "10001",
                "USA",
                CustomerStatus.ACTIVE);

        var requestCounter = meterRegistry.counter("update_customer_endpoint_count");
        double initialCount = requestCounter.count();

        // test
        mockMvc.perform(MockMvcRequestBuilders.put("/customers/update/" + updatedCustomer.getId())
                        .header("Authorization", "Bearer " + managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerUpdateDto)))
                .andExpect(status().isOk())
                .andReturn();

        // assert
        assertThat(dbUtil.customerExistsByEmail("tony.stark@example.com")).isTrue();
        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);

        Customer actualCustomer = customerRepository.findCustomerByEmail("tony.stark@example.com")
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        assertThat(actualCustomer.getId()).isNotNull();
        assertThat(actualCustomer.getFirstName()).isEqualTo("Tony");
        assertThat(actualCustomer.getLastName()).isEqualTo("Stark");
        assertThat(actualCustomer.getEmail()).isEqualTo("tony.stark@example.com");
        assertThat(actualCustomer.getPhoneNumber()).isEqualTo("+1234567890");
        assertThat(actualCustomer.getTaxNumber()).isEqualTo("234567891");
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
                        "Password!1",
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
        mockMvc.perform(MockMvcRequestBuilders.delete("/customers/delete/" + expectedCustomer.getId())
                        .header("Authorization", "Bearer " + managerToken)
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

    private void createCustomerAndExpect(String firstName, String lastName, String email, String password, String phoneNumber,
                                         String taxNumber, String address, String city, String zipCode,
                                         String country, int expectedStatus, String expectedResponse) throws Exception {
        CustomerCreateDto newCustomerDto = new CustomerCreateDto(firstName, lastName, email, password, phoneNumber,
                taxNumber, address, city, zipCode, country);

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .header("Authorization", "Bearer " + managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCustomerDto)))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().string(expectedResponse));
    }
}