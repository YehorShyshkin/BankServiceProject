package de.yehorsh.customerservice.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import de.yehorsh.authservice.model.entity.Role;
import de.yehorsh.authservice.model.entity.User;
import de.yehorsh.authservice.model.enums.RoleName;
import de.yehorsh.authservice.model.enums.UserStatus;
import de.yehorsh.authservice.repository.RoleRepository;
import de.yehorsh.authservice.repository.UserRepository;
import de.yehorsh.customerservice.CustomerServiceApplication;
import de.yehorsh.customerservice.config.ContainersEnvironment;
import de.yehorsh.customerservice.controller.DBUtil;
import de.yehorsh.customerservice.dto.CustomerCreateDto;
import de.yehorsh.customerservice.dto.CustomerDto;
import de.yehorsh.customerservice.dto.CustomerUpdateDto;
import de.yehorsh.customerservice.exception.CustomerNotFoundException;
import de.yehorsh.customerservice.exception.DuplicateFieldException;
import de.yehorsh.customerservice.model.Customer;
import de.yehorsh.customerservice.model.enums.CustomerStatus;
import de.yehorsh.customerservice.repository.CustomerRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.Assert.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(classes = CustomerServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {ContainersEnvironment.Initializer.class})
@AutoConfigureWireMock
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CustomerServiceTest {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DBUtil dbUtil;
    @Autowired
    private MeterRegistry meterRegistry;
    private WireMockServer wireMockServer;


    @BeforeEach
    void cleanUpDatabase() {
        customerRepository.deleteAll();
        dbUtil = new DBUtil(dataSource);
        meterRegistry.clear();
        wireMockServer = new WireMockServer(8084);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8084);

        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/users/create"))
                .willReturn(WireMock.aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": \"12345\"}"))
        );
    }

    @BeforeAll
    static void setUpDatabase(@Autowired UserRepository userRepository, @Autowired RoleRepository roleRepository) {
        // Ensure roles exist
        for (RoleName roleName : RoleName.values()) {
            if (!roleRepository.existsByName(roleName.name())) {
                Role role = new Role();
                role.setId(UUID.randomUUID());
                role.setName(String.valueOf(roleName));
                roleRepository.save(role);
            }
        }

        // Add manager user
        Role managerRole = roleRepository.findByName("MANAGER")
                .orElseThrow(() -> new RuntimeException("MANAGER role not found"));

        userRepository.save(User.builder()
                .email("manager@example.com")
                .password(new BCryptPasswordEncoder().encode("StrongManagerP@ssw0rd!"))
                .roles(managerRole)
                .status(UserStatus.ACTIVATED)
                .build());

        // Add admin user
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

        userRepository.save(User.builder()
                .email("admin@example.com")
                .password(new BCryptPasswordEncoder().encode("StrongAdminP@ssw0rd!"))
                .roles(adminRole)
                .status(UserStatus.NEW)
                .build());

        // Add customer user
        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("CUSTOMER role not found"));

        userRepository.save(User.builder()
                .email("customer@example.com")
                .password(new BCryptPasswordEncoder().encode("StrongCustomer@ssw0rd!"))
                .roles(customerRole)
                .status(UserStatus.NEW)
                .build());
    }

    @AfterEach
    void cleanUp() {
        wireMockServer.stop();
    }

    @WithMockUser(username = "manager@example.com", authorities = "MANAGER")
    @ParameterizedTest
    @CsvSource({
            // Duplicate email
            "'John', 'Doe', 'john.doe@example.com', 'Password1!', '+123 456 7890', '1234567890', " +
                    "'123 Main St.', 'Springfield', '12345', 'USA', " +
                    "'Jane', 'Smith', 'john.doe@example.com', 'Password1!', '+987 654 3210', '0987654321', " +
                    "'456 Elm St.', 'Shelbyville', '54321', 'USA', " +
                    "'Customer with the provided details already exists'",

            // Duplicate phone number
            "'John', 'Doe', 'john.doe@example.com', 'Password1!', '+123 456 7890', '1234567890', " +
                    "'123 Main St.', 'Springfield', '12345', 'USA', " +
                    "'Jane', 'Smith', 'jane.smith@example.com', 'Password1!', '+123 456 7890', '0987654321', " +
                    "'456 Elm St.', 'Shelbyville', '54321', 'USA', " +
                    "'Customer with the provided details already exists'",

            // Duplicate tax number
            "'John', 'Doe', 'john.doe@example.com', 'Password1!', '+123 456 7890', '1234567890', " +
                    "'123 Main St.', 'Springfield', '12345', 'USA', " +
                    "'Jane', 'Smith', 'jane.smith@example.com', 'Password1!', '+987 654 3210', '1234567890', " +
                    "'456 Elm St.', 'Shelbyville', '54321', 'USA', " +
                    "'Customer with the provided details already exists'"
    })
    void test_createCustomerDuplicateFields(
            String firstName1, String lastName1, String email1, String password1, String phoneNumber1, String taxNumber1,
            String address1, String city1, String zipCode1, String country1,
            String firstName2, String lastName2, String email2, String password2, String phoneNumber2, String taxNumber2,
            String address2, String city2, String zipCode2, String country2,
            String expectedErrorMessage) {

        // prepare
        CustomerCreateDto customerCreateDto1 = new CustomerCreateDto(
                firstName1, lastName1, email1, password1, phoneNumber1, taxNumber1,
                address1, city1, zipCode1, country1
        );
        customerService.createNewCustomer(customerCreateDto1);

        CustomerCreateDto duplicateCustomer = new CustomerCreateDto(
                firstName2, lastName2, email2, password2, phoneNumber2, taxNumber2,
                address2, city2, zipCode2, country2
        );

        // test
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                customerService.createNewCustomer(duplicateCustomer)
        );

        // assert
        assertThat(exception.getMessage()).isEqualTo(expectedErrorMessage);
    }

    @Test
    @WithMockUser(username = "manager@example.com", authorities = "MANAGER")
    void test_createCustomer_success() throws SQLException {
        // prepare
        var requestCounter = meterRegistry.counter("create_customer_service_count");
        double initialCount = requestCounter.count();
        CustomerCreateDto customerCreateDto = new CustomerCreateDto(
                "Clark",
                "Kent",
                "clark.kent@example.com",
                "Password123!",
                "+1234567890",
                "123456789",
                "123 Main Street",
                "Metropolis",
                "54321",
                "USA"
        );

        // test
        assertThatCode(() -> customerService.createNewCustomer(customerCreateDto))
                .doesNotThrowAnyException();

        // assert
        Customer createdCustomer = customerRepository.findCustomerByEmail("clark.kent@example.com").get();
        assertThat(createdCustomer.getFirstName()).isEqualTo("Clark");
        assertThat(createdCustomer.getLastName()).isEqualTo("Kent");
        assertThat(createdCustomer.getEmail()).isEqualTo("clark.kent@example.com");
        assertThat(createdCustomer.getPhoneNumber()).isEqualTo("+1234567890");
        assertThat(createdCustomer.getTaxNumber()).isEqualTo("123456789");
        assertThat(createdCustomer.getAddress()).isEqualTo("123 Main Street");
        assertThat(createdCustomer.getCity()).isEqualTo("Metropolis");
        assertThat(createdCustomer.getCountry()).isEqualTo("USA");

        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
        assertThat(dbUtil.customerExistsByEmail("clark.kent@example.com")).isTrue();
    }

    @Test
    @WithMockUser(username = "manager@example.com", authorities = "MANAGER")
    void test_findCustomer_success() {
        // prepare
        Customer expectedCustomer = customerService.createNewCustomer(
                new CustomerCreateDto(
                        "Clark",
                        "Kent",
                        "clark.kent@example.com",
                        "Password123!",
                        "+1234567890",
                        "123456789",
                        "123 Main Street",
                        "Metropolis",
                        "54321",
                        "USA"
                ));

        var requestCounter = meterRegistry.counter("find_customer_by_id_service_count");
        double initialCount = requestCounter.count();

        // test
        Customer foundCustomer = customerService.findCustomerById(expectedCustomer.getId());

        // assert
        assertThat(foundCustomer.getId()).isNotNull();
        assertThat(foundCustomer.getFirstName()).isEqualTo("Clark");
        assertThat(foundCustomer.getLastName()).isEqualTo("Kent");
        assertThat(foundCustomer.getEmail()).isEqualTo("clark.kent@example.com");
        assertThat(foundCustomer.getPhoneNumber()).isEqualTo("+1234567890");
        assertThat(foundCustomer.getTaxNumber()).isEqualTo("123456789");
        assertThat(foundCustomer.getAddress()).isEqualTo("123 Main Street");
        assertThat(foundCustomer.getCity()).isEqualTo("Metropolis");
        assertThat(foundCustomer.getZipCode()).isEqualTo("54321");
        assertThat(foundCustomer.getCountry()).isEqualTo("USA");

        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = "MANAGER")
    void test_findAllCustomer_success() throws Exception {
        customerService.createNewCustomer(
                new CustomerCreateDto(
                        "Tony",
                        "Stark",
                        "tony.stark@example.com",
                        "Password123!",
                        "+1098765432",
                        "123456789",
                        "10880 Malibu Point",
                        "Malibu",
                        "90265",
                        "USA"));
        customerService.createNewCustomer(
                new CustomerCreateDto(
                        "Peter",
                        "Parker",
                        "peter.parker@example.com",
                        "Password123!",
                        "+1234567890",
                        "564738292",
                        "15 Queens Blvd",
                        "New York",
                        "10001",
                        "USA"));

        var requestCounter = meterRegistry.counter("find_allCustomer_service_count");
        double initialCount = requestCounter.count();

        // test
        List<CustomerDto> foundCustomers = customerService.findAllCustomers();

        // assert
        assertThat(foundCustomers).hasSize(2);
        assertThat(dbUtil.customerExistsByEmail("tony.stark@example.com")).isTrue();
        assertThat(dbUtil.customerExistsByEmail("peter.parker@example.com")).isTrue();
        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
    }

    @Test
    @WithMockUser(username = "manager@example.com", authorities = "MANAGER")
    void test_findNotExistingCustomer_exception() {
        // prepare
        UUID customerId = UUID.randomUUID();

        // test
        Exception exception = assertThrows(CustomerNotFoundException.class, () ->
                customerService.findCustomerById(customerId));

        // assert
        assertThat(exception.getMessage()).isEqualTo("Customer with Id: " + customerId + " not found");
    }

    @Test
    @WithMockUser(username = "manager@example.com", authorities = "MANAGER")
    void test_updateCustomer_success() throws SQLException {
        // prepare
        Customer expectedCustomer = customerService.createNewCustomer(
                new CustomerCreateDto(
                        "Clark",
                        "Kent",
                        "clark.kent@example.com",
                        "Password123!",
                        "+1234567890",
                        "123456789",
                        "123 Main Street",
                        "Metropolis",
                        "54321",
                        "USA"));

        UUID customerId = customerService.findCustomerById(expectedCustomer.getId()).getId();

        var requestCounter = meterRegistry.counter("update_customer_service_count");
        double initialCount = requestCounter.count();

        CustomerUpdateDto actualCustomer = new CustomerUpdateDto(
                customerId,
                "John",
                "Smith",
                "john.smith@example.com",
                "+987654321",
                "12343414341",
                "456 Elm Street",
                "Gotham",
                "65432",
                "USA",
                CustomerStatus.CLOSED);

        // test
        customerService.updateCustomer(expectedCustomer.getId(), actualCustomer);

        // assert
        Customer updatedCustomer = customerRepository.findCustomerByEmail("john.smith@example.com").get();

        assertThat(updatedCustomer.getId()).isNotNull();
        assertThat(updatedCustomer.getFirstName()).isEqualTo("John");
        assertThat(updatedCustomer.getLastName()).isEqualTo("Smith");
        assertThat(updatedCustomer.getTaxNumber()).isEqualTo("12343414341");
        assertThat(updatedCustomer.getEmail()).isEqualTo("john.smith@example.com");
        assertThat(updatedCustomer.getPhoneNumber()).isEqualTo("+987654321");
        assertThat(updatedCustomer.getAddress()).isEqualTo("456 Elm Street");
        assertThat(updatedCustomer.getCity()).isEqualTo("Gotham");
        assertThat(updatedCustomer.getZipCode()).isEqualTo("65432");
        assertThat(updatedCustomer.getCountry()).isEqualTo("USA");
        assertThat(updatedCustomer.getCustomerStatus()).isEqualTo(CustomerStatus.CLOSED);
        assertThat(updatedCustomer.getUpdatedAt()).isNotNull();

        assertThat(dbUtil.customerExistsByEmail("john.smith@example.com")).isTrue();
        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
    }

    @Test
    @WithMockUser(username = "manager@example.com", authorities = "MANAGER")
    void test_updateCustomerWithDuplicatePhoneNumber_exception() {
        // prepare
        Customer existingCustomer = customerService.createNewCustomer(
                new CustomerCreateDto(
                        "Clark",
                        "Kent",
                        "clark.kent@example.com",
                        "Password123!",
                        "+123434567890",
                        "123456789",
                        "123 Main Street",
                        "Metropolis",
                        "54321",
                        "USA"));

        UUID customerId = customerService.findCustomerById(existingCustomer.getId()).getId();

        CustomerUpdateDto updateDto = new CustomerUpdateDto(
                customerId,
                "John",
                "Smith",
                "unique.email@example.com",
                "+123434567890",
                "12343414341",
                "456 Elm Street",
                "Gotham",
                "65432",
                "USA",
                CustomerStatus.CLOSED);

        // test
        DuplicateFieldException exception = assertThrows(DuplicateFieldException.class,
                () -> customerService.updateCustomer(customerId, updateDto));

        // assert
        assertThat(exception.getMessage()).isEqualTo("This phone number is already registered to another account");
    }

    @Test
    @WithMockUser(username = "manager@example.com", authorities = "MANAGER")
    void test_updateCustomerWithDuplicateEmail_exception() {
        // prepare
        Customer expectedCustomer = customerService.createNewCustomer(
                new CustomerCreateDto(
                        "Clark",
                        "Kent",
                        "clark.kent@example.com",
                        "Password123!",
                        "+123434567890",
                        "123456789",
                        "123 Main Street",
                        "Metropolis",
                        "54321",
                        "USA"));

        UUID customerId = customerService.findCustomerById(expectedCustomer.getId()).getId();

        CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto(
                customerId,
                "John",
                "Smith",
                "clark.kent@example.com",
                "+111111111111",
                "uniqueTax123",
                "456 Elm Street",
                "Gotham",
                "65432",
                "USA",
                CustomerStatus.CLOSED);

        // test
        DuplicateFieldException exception = assertThrows(DuplicateFieldException.class, () ->
                customerService.updateCustomer(customerId, customerUpdateDto)
        );

        // assert
        assertThat(exception.getMessage()).isEqualTo("This email is already registered to another account");
    }

    @Test
    @WithMockUser(username = "manager@example.com", authorities = "MANAGER")
    void test_updateCustomerWithDuplicateTaxNumber_exception() {
        // prepare
        var duplicateTaxNumber = "1t672263";
        Customer existingCustomer = customerService.createNewCustomer(
                new CustomerCreateDto(
                        "Bruce",
                        "Wayne",
                        "bruce.wayne@example.com",
                        "Password123!",
                        "+19876543210",
                        duplicateTaxNumber,
                        "1007 Mountain Drive",
                        "Gotham",
                        "12345",
                        "USA"));

        UUID customerId = customerService.findCustomerById(existingCustomer.getId()).getId();

        CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto(
                customerId,
                "John",
                "Doe",
                "john.doe@example.com",
                "+1112223344",
                duplicateTaxNumber,
                "123 Elm Street",
                "Metropolis",
                "54321",
                "USA",
                CustomerStatus.ACTIVE);

        // test
        DuplicateFieldException exception = assertThrows(DuplicateFieldException.class, () ->
                customerService.updateCustomer(customerId, customerUpdateDto)
        );

        // assert
        assertThat(exception.getMessage()).isEqualTo("This tax number is already registered to another account");
    }

    @Test
    @WithMockUser(username = "manager@example.com", authorities = "MANAGER")
    void test_deleteCustomer_success() throws SQLException {
        // prepare
        var requestCounter = meterRegistry.counter("delete_customer_service_count");
        double initialCount = requestCounter.count();

        Customer expectedCustomer = customerService.createNewCustomer(
                new CustomerCreateDto(
                        "Clark",
                        "Kent",
                        "clark.kent@example.com",
                        "Password123!",
                        "+1234567890",
                        "123456789",
                        "123 Main Street",
                        "Metropolis",
                        "54321",
                        "USA"));

        UUID customerId = customerService.findCustomerById(expectedCustomer.getId()).getId();

        // test
        customerService.deleteCustomer(customerId);

        // assert
        assertThat(dbUtil.customerExistsByEmail("clark.kent@example.com")).isFalse();
        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
    }

    @Test
    @WithMockUser(username = "manager@example.com", authorities = "MANAGER")
    void test_deleteNotExistingCustomer_exception() {
        // prepare
        UUID customerId = UUID.randomUUID();

        // test
        Exception exception = Assertions.assertThrows(CustomerNotFoundException.class, () ->
                customerService.deleteCustomer(customerId)
        );

        // assert
        assertThat(exception.getMessage()).isEqualTo("Customer with Id: " + customerId + " not found");
    }

    @Test
    @WithMockUser(username = "manager@example.com", authorities = "MANAGER")
    void test_deleteCustomerWithNullCustomerId_exception() {
        // test
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                customerService.deleteCustomer(null)
        );

        // assert
        assertThat(exception.getMessage()).isEqualTo("Customer id cannot be null");
    }
}