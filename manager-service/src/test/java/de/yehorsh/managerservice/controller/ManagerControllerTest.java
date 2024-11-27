package de.yehorsh.managerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import de.yehorsh.authservice.dto.UserDto;
import de.yehorsh.managerservice.ManagerServiceApplication;
import de.yehorsh.managerservice.config.ContainersEnvironment;
import de.yehorsh.managerservice.dto.ManagerCreateDto;
import de.yehorsh.managerservice.dto.ManagerUpdateDto;
import de.yehorsh.managerservice.exception.ManagerNotFoundException;
import de.yehorsh.managerservice.model.Manager;
import de.yehorsh.managerservice.model.enums.ManagerStatus;
import de.yehorsh.managerservice.repository.ManagerRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(classes = ManagerServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {ContainersEnvironment.Initializer.class})
@AutoConfigureWireMock
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ManagerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DBUtil dbUtil;

    @Autowired
    private MeterRegistry meterRegistry;

    private WireMockServer wireMockServer;

    @BeforeEach
    void cleanUpDatabase() {
        managerRepository.deleteAll();
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

     @Sql(statements = {
            "INSERT INTO roles (id, name) SELECT uuid_generate_v4(), 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN')",
            "INSERT INTO users (id, email, password, status) SELECT uuid_generate_v4(), 'admin@example.com', " +
                    "'$2a$10$wqQkDGFQ6IX94uhdkB8YuOql8Q3Q32ftCwL5OUn9XvBhVzz5hvJOm', 'NEW' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@example.com')"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

    @AfterEach
    void cleanUp() {
        wireMockServer.stop();
    }

    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    @ParameterizedTest
    @CsvSource({
            // Valid manager details
            "'Bruce', 'Wayne', 'bruce.wayne@example.com', '+1234567891', 'Password123!', 201, 'Manager was successfully created'",
            // Missing first name
            "'', 'Doe', 'john.doe@example.com', '+123456789', 'Password123!', 400, 'Can not be empty; " +
                    "Invalid first name: only alphabetic characters are allowed'",
            // Missing last name
            "'John', '', 'john.doe@example.com', '+123456789', 'Password123!', 400, 'Can not be empty; " +
                    "Invalid last name: only alphabetic characters are allowed'",
            // Missing email
            "'John', 'Doe', '', '+123456789', 'Password123!', 400, 'Email should not be empty; Invalid email address'",
            // Missing phone number
            "'John', 'Doe', 'john.doe@example.com', '', 'Password123!', 400, 'Invalid phone number: " +
                    "it must be in a valid format (e.g., +123456789, (123) 456-7890, 123-456-7890)'",
            // Invalid email format
            "'John', 'Doe', 'invalid-email', '+123456789', 'Password123!', 400, 'Email should be valid; Invalid email address'",
            // Invalid phone number format
            "'John', 'Doe', 'john.doe@example.com', 'invalid-phone', 'Password123!', 400, 'Invalid phone number: " +
                    "it must be in a valid format (e.g., +123456789, (123) 456-7890, 123-456-7890)'",
            // Invalid password format (too short)
            "'John', 'Doe', 'john.doe@example.com', '+123456789', 'short', 400, 'Password must be between 8 and 64 characters; " +
                    "The password is incorrect. Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'",
            // Invalid password format (no numbers)
            "'John', 'Doe', 'john.doe@example.com', '+123456789', 'NoNumber!', 400, 'The password is incorrect. " +
                    "Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'",
            // Invalid password format (no special characters)
            "'John', 'Doe', 'john.doe@example.com', '+123456789', 'NoSpecial1', 400, 'The password is incorrect. " +
                    "Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'",
            // Invalid password format (no uppercase)
            "'John', 'Doe', 'john.doe@example.com', '+123456789', 'lowercase1!', 400, 'The password is incorrect. " +
                    "Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'"
    })
    void test_createManager_invalidCases(String firstName, String lastName, String email, String phone, String password,
                                         int expectedStatus, String expectedMessage) throws Exception {
        createManagerAndExpect(firstName, lastName, email, phone, password, expectedStatus, expectedMessage);
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void test_createManager_success() throws Exception {
        // prepare
        var requestCounter = meterRegistry.counter("create_manager_endpoint_count");
        double initialCount = requestCounter.count();

        var requestTimer = meterRegistry.timer("create_manager_endpoint_timer");
        long initialTimeCount = requestTimer.count();

        ManagerCreateDto managerCreateDto = new ManagerCreateDto(
                "Bruce",
                "Wayne",
                "bruce.wayne@example.com",
                "+1234567891",
                "Password123!"
        );

        // test
        String managerCreateJson = objectMapper.writeValueAsString(managerCreateDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(managerCreateJson))
                .andExpect(status().isCreated())
                .andReturn();

        UserDto expectedUserDto = new UserDto(
                managerCreateDto.email(),
                "Password123!",
                "MANAGER"
        );

        wireMockServer.verify(postRequestedFor(urlEqualTo("/users/create"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(expectedUserDto))));

        assertThat(dbUtil.managerExistsByEmail(managerCreateDto.email())).isTrue();

        // assert
        Manager createdManager = managerRepository.findManagerByEmail(managerCreateDto.email())
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found"));

        assertThat(createdManager.getFirstName()).isEqualTo(managerCreateDto.firstName());
        assertThat(createdManager.getLastName()).isEqualTo(managerCreateDto.lastName());
        assertThat(createdManager.getEmail()).isEqualTo(managerCreateDto.email());
        assertThat(createdManager.getPhoneNumber()).isEqualTo(managerCreateDto.phoneNumber());
        assertThat(createdManager.getId()).isNotNull();
        assertThat(createdManager.getCreationDate()).isNotNull();
        assertThat(createdManager.getUpdateDate()).isNotNull();

        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
        assertThat(requestTimer.count()).isEqualTo(initialTimeCount + 1);
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void test_createManagerWithSameLastName_badRequest() throws Exception {
        // prepare
        ManagerCreateDto firstManagerDto = new ManagerCreateDto(
                "John",
                "Weak",
                "john@example.com",
                "+1234567891",
                "fdfdfs1fd1_A"
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstManagerDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Manager was successfully created"));

        ManagerCreateDto secondManagerDto = new ManagerCreateDto(
                "John",
                "Weak",
                "john.weak@example.com",
                "+123456789000",
                "fdfdfs1fd1_A"
        );

        // test
        mockMvc.perform(MockMvcRequestBuilders.post("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondManagerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"httpStatus\":\"BAD_REQUEST\",\"message\":\"Manager with the provided details already exists\"}"));

        // assert
        assertThat(dbUtil.managerExistsByEmail("john.weak@example.com")).isFalse();
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void test_findManager_success() throws Exception {
        // prepare
        ManagerCreateDto managerCreateDto = new ManagerCreateDto(
                "John",
                "Weak",
                "john@example.com",
                "+1234567891",
                "fdfdfs1fd1_A"
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(managerCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Manager was successfully created"))
                .andReturn();

        Manager createdManager = managerRepository.findManagerByEmail("john@example.com")
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found"));

        var requestCounter = meterRegistry.counter("find_manager_endpoint_count");
        double initialCount = requestCounter.count();

        // test
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/" + createdManager.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // assert
        assertThat(dbUtil.managerExistsByEmail("john@example.com")).isTrue();

        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void test_updateManager_success() throws Exception {
        // prepare
        ManagerCreateDto managerCreateDto = new ManagerCreateDto(
                "John",
                "Doe",
                "john.doe@example.com",
                "+123456789",
                "fdfdfs1fd1_A"
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(managerCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Manager was successfully created"));

        Manager createdManager = managerRepository.findManagerByEmail("john.doe@example.com")
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found"));

        // test
        ManagerUpdateDto managerUpdateDto = new ManagerUpdateDto(
                createdManager.getId(),
                "Alisa",
                "First",
                "alisa.first@example.com",
                "+1234567811",
                ManagerStatus.ACTIVE
        );

        wireMockServer.stubFor(put(urlEqualTo("/users/update"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"User updated successfully\"}")));

        var requestCounter = meterRegistry.counter("update_manager_endpoint_count");
        double initialCount = requestCounter.count();

        String managerJson = objectMapper.writeValueAsString(managerUpdateDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/managers/" + createdManager.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(managerJson))
                .andExpect(status().isOk());

        // assert
        assertThat(dbUtil.managerExistsByEmail("alisa.first@example.com")).isTrue();
        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);

        Manager updatedManager = managerRepository.findById(createdManager.getId())
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found"));

        assertThat(updatedManager.getFirstName()).isEqualTo("Alisa");
        assertThat(updatedManager.getLastName()).isEqualTo("First");
        assertThat(updatedManager.getEmail()).isEqualTo("alisa.first@example.com");
        assertThat(updatedManager.getPhoneNumber()).isEqualTo("+1234567811");
        assertThat(updatedManager.getManagerStatus()).isEqualTo(ManagerStatus.ACTIVE);
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void test_deleteManager_success() throws Exception {
        // prepare
        ManagerCreateDto expectedManager = new ManagerCreateDto(
                "John",
                "Doe",
                "john.doe@example.com",
                "+123456789",
                "fdfdfs1fd1_A"
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedManager)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Manager was successfully created"));


        var requestCounter = meterRegistry.counter("delete_manager_endpoint_count");
        double initialCount = requestCounter.count();

        String managerJson = objectMapper.writeValueAsString(expectedManager);

        Manager createdManager = managerRepository.findManagerByEmail("john.doe@example.com")
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found"));

        // test
        mockMvc.perform(MockMvcRequestBuilders.delete("/managers/" + createdManager.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(managerJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Manager with ID " + createdManager.getId() + " was deleted"));

        // assert
        assertThat(dbUtil.managerExistsByEmail("john.doe@example.com")).isFalse();
        assertThat(managerRepository.existsById(createdManager.getId()))
                .as("Manager should be deleted").isFalse();
        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
    }

    private void createManagerAndExpect(String firstName, String lastName, String email, String phone, String password,
                                        int expectedStatus, String expectedMessage) throws Exception {

        ManagerCreateDto newManagerDto = new ManagerCreateDto(firstName, lastName, email, phone, password);
        String managerJson = objectMapper.writeValueAsString(newManagerDto);

        wireMockServer.stubFor(post(urlEqualTo("/users/create"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": \"12345\"}")));

        mockMvc.perform(MockMvcRequestBuilders.post("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(managerJson))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().string(expectedMessage));

        if (expectedStatus == 201) {
            assertThat(dbUtil.managerExistsByEmail(email)).isTrue();
        }
    }
}