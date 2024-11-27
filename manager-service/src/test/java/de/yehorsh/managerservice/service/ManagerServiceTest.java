package de.yehorsh.managerservice.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import de.yehorsh.managerservice.ManagerServiceApplication;
import de.yehorsh.managerservice.config.ContainersEnvironment;
import de.yehorsh.managerservice.controller.DBUtil;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(classes = ManagerServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {ContainersEnvironment.Initializer.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ManagerServiceTest {
    @Autowired
    private ManagerService managerService;

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
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void test_createManager_success() throws SQLException {
        // prepare
        var requestCounter = meterRegistry.counter("create_manager_service_count");
        double initialCount = requestCounter.count();

        ManagerCreateDto managerCreateDto = new ManagerCreateDto(
                "Alice",
                "Smith",
                "alice.smith@example.com",
                "+123456789",
                "Password123!"
        );

        // test
        assertThatCode(() -> managerService.createNewManager(managerCreateDto))
                .doesNotThrowAnyException();

        Manager createdManager = managerRepository.findManagerByEmail("alice.smith@example.com").get();

        // assert
        assertThat(createdManager.getFirstName()).isEqualTo("Alice");
        assertThat(createdManager.getLastName()).isEqualTo("Smith");
        assertThat(createdManager.getEmail()).isEqualTo("alice.smith@example.com");
        assertThat(createdManager.getPhoneNumber()).isEqualTo("+123456789");
        assertThat(createdManager.getId()).isNotNull();
        assertThat(createdManager.getCreationDate()).isNotNull();
        assertThat(createdManager.getUpdateDate()).isNotNull();

        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
        assertThat(dbUtil.managerExistsByEmail("alice.smith@example.com")).isTrue();
    }

    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    @ParameterizedTest
    @CsvSource({
            // Test cases for duplicate fields
            "'Alice', 'Smith', 'alice.smith@example.com', '+123456789', 'Password123!', 'Bob', 'Dilan', " +
                    "'alice.smith@example.com', '+987654321', 'Password123!', 'Manager with the provided details already exists'"
    })
    void test_createManagerDuplicateFields(
            String firstName1, String lastName1, String email1, String phone1, String userId1,
            String firstName2, String lastName2, String email2, String phone2, String userId2,
            String expectedErrorMessage) {

        ManagerCreateDto managerCreateDto1 = new ManagerCreateDto(firstName1, lastName1, email1, phone1, userId1);
        managerService.createNewManager(managerCreateDto1);

        ManagerCreateDto managerCreateDto2 = new ManagerCreateDto(firstName2, lastName2, email2, phone2, userId2);
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                managerService.createNewManager(managerCreateDto2)
        );
        assertThat(exception.getMessage()).isEqualTo(expectedErrorMessage);
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void test_findManager_success() {
        // prepare
        Manager expectedManager = managerService.createNewManager(
                new ManagerCreateDto(
                        "Bob",
                        "Dilan",
                        "bob.smith@example.com",
                        "+123456789",
                        "fdfdfs1fd1_A"));

        var requestCounter = meterRegistry.counter("find_manager_by_id_service_count");
        double initialCount = requestCounter.count();

        // test
        Manager foundManager = managerService.findManagerById(expectedManager.getId());

        // assert
        assertThat(foundManager).isNotNull();
        assertThat(foundManager.getId()).isEqualTo(expectedManager.getId());
        assertThat(foundManager.getFirstName()).isEqualTo("Bob");
        assertThat(foundManager.getLastName()).isEqualTo("Dilan");
        assertThat(foundManager.getEmail()).isEqualTo("bob.smith@example.com");
        assertThat(foundManager.getPhoneNumber()).isEqualTo("+123456789");

        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void test_findManagerNotExistingManagerException() {
        // prepare
        UUID managerId = UUID.randomUUID();

        // test
        Exception exception = assertThrows(ManagerNotFoundException.class, () ->
                managerService.findManagerById(managerId)
        );

        // assert
        assertThat(exception.getMessage()).isEqualTo("Manager with Id: " + managerId + " not found");
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void test_updateManager_success() throws SQLException {
        // prepare
        Manager expectedManager = managerService.createNewManager(
                new ManagerCreateDto(
                        "Bob",
                        "Dilan",
                        "bob.smith@example.com",
                        "+123456789",
                        "fdfdfs1fd1_A"));

        UUID managerId = managerService.findManagerById(expectedManager.getId()).getId();

        var requestCounter = meterRegistry.counter("update_manager_service_count");
        double initialCount = requestCounter.count();

        ManagerUpdateDto actualManager = new ManagerUpdateDto(
                managerId,
                "Smith",
                "Dilan",
                "jane.smith@example.com",
                "+987654321",
                ManagerStatus.INACTIVE
        );

        // test
        managerService.updateManager(expectedManager.getId(), actualManager);

        // assert
        Manager updatedManager = managerRepository.findManagerByEmail("jane.smith@example.com").get();

        assertThat(updatedManager.getId()).isEqualTo(managerId);
        assertThat(updatedManager.getFirstName()).isEqualTo("Smith");
        assertThat(updatedManager.getLastName()).isEqualTo("Dilan");
        assertThat(updatedManager.getEmail()).isEqualTo("jane.smith@example.com");
        assertThat(updatedManager.getPhoneNumber()).isEqualTo("+987654321");
        assertThat(updatedManager.getManagerStatus()).isEqualTo(ManagerStatus.INACTIVE);
        assertThat(updatedManager.getUpdateDate()).isNotNull();

        assertThat(dbUtil.managerExistsByEmail("jane.smith@example.com")).isTrue();
        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void test_deleteManager_success() throws SQLException {
        // prepare
        var requestCounter = meterRegistry.counter("delete_manager_service_count");
        double initialCount = requestCounter.count();

        Manager expectedManager = managerService.createNewManager(
                new ManagerCreateDto(
                        "Bob",
                        "Dilan",
                        "bob.smith@example.com",
                        "+123456789",
                        "fdfdfs1fd1_A"));

        UUID managerId = managerService.findManagerById(expectedManager.getId()).getId();

        // test
        managerService.deleteManager(managerId);

        // assert
        assertThat(dbUtil.managerExistsByEmail("bob.smith@example.com")).isFalse();
        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void test_deleteManagerNotExistingManager_exception() {
        // prepare
        UUID managerId = UUID.randomUUID();

        // test
        Exception exception = assertThrows(ManagerNotFoundException.class, () ->
                managerService.deleteManager(managerId)
        );

        // assert
        assertThat(exception.getMessage()).isEqualTo("Manager with Id: " + managerId + " not found");
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void test_deleteManagerWithNullManagerId_exception() {
        // test
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                managerService.deleteManager(null)
        );

        // assert
        assertThat(exception.getMessage()).isEqualTo("Manager Id cannot be null");
    }
}