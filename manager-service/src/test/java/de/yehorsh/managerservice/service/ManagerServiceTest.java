package de.yehorsh.managerservice.service;
import de.yehorsh.managerservice.ManagerServiceApplication;
import de.yehorsh.managerservice.config.ContainersEnvironment;
import de.yehorsh.managerservice.controller.DBUtil;
import de.yehorsh.managerservice.dto.ManagerCreateDto;
import de.yehorsh.managerservice.dto.ManagerUpdateDto;
import de.yehorsh.managerservice.exception.ManagerNotFoundException;
import de.yehorsh.managerservice.model.Manager;
import de.yehorsh.managerservice.model.enums.ManagerStatus;
import de.yehorsh.managerservice.repository.ManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

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
class ManagerServiceTest {
    @Autowired
    private ManagerService managerService;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private DataSource dataSource;
    private DBUtil dbUtil;

    @BeforeEach
    void cleanUpDatabase() {
        managerRepository.deleteAll();
        dbUtil = new DBUtil(dataSource);
    }

    @Test
    void test_createManager_success() throws SQLException {
        // prepare
        ManagerCreateDto managerCreateDto = new ManagerCreateDto(
                "Alice",
                "Smith",
                "alice.smith@example.com",
                "+123456789"
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

        assertThat(dbUtil.managerExistsByEmail("alice.smith@example.com")).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            // Testing: Two managers with the same first name, last name, and email
            "'Alice', 'Smith', 'alice.smith@example.com', '+123456789', 'Bob', 'Dilan', " +
                    "'alice.smith@example.com', '+987654321', 'Manager with the provided details already exists.'",

            // Testing: Two managers with the same email but different last names
            "'Alice', 'Smith', 'alice.smith@example.com', '+123456789', 'Bob', 'Smith', " +
                    "'bob.smith@example.com', '+987654321', 'Manager with the provided details already exists.'",

            // Testing: Two managers with the same email and phone number but different names
            "'Alice', 'Smith', 'alice.smith@example.com', '+123456789', 'Bob', 'Dilan', " +
                    "'bob.smith@example.com', '+123456789', 'Manager with the provided details already exists.'"
    })
    void test_createManagerDuplicateFields(
            String firstName1, String lastName1, String email1, String phone1,
            String firstName2, String lastName2, String email2, String phone2,
            String expectedErrorMessage) {

        ManagerCreateDto managerCreateDto1 = new ManagerCreateDto(firstName1, lastName1, email1, phone1);
        managerService.createNewManager(managerCreateDto1);

        ManagerCreateDto managerCreateDto2 = new ManagerCreateDto(firstName2, lastName2, email2, phone2);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            managerService.createNewManager(managerCreateDto2);
        });

        assertThat(exception.getMessage()).isEqualTo(expectedErrorMessage);
    }

    @Test
    void test_findManager_success() {
        // prepare
        Manager expectedManager = managerService.createNewManager(
                new ManagerCreateDto(
                        "Bob",
                        "Dilan",
                        "bob.smith@example.com",
                        "+123456789"));

        // test
        Manager foundManager = managerService.findManagerById(expectedManager.getId());

        // assert
        assertThat(foundManager).isNotNull();
        assertThat(foundManager.getId()).isEqualTo(expectedManager.getId());
        assertThat(foundManager.getFirstName()).isEqualTo("Bob");
        assertThat(foundManager.getLastName()).isEqualTo("Dilan");
        assertThat(foundManager.getEmail()).isEqualTo("bob.smith@example.com");
        assertThat(foundManager.getPhoneNumber()).isEqualTo("+123456789");
    }

    @Test
    void test_findManagerNotExistingManagerException() {
        // prepare
        UUID managerId = UUID.randomUUID();

        // test
        Exception exception = assertThrows(ManagerNotFoundException.class, () -> {
            managerService.findManagerById(managerId);
        });

        // assert
        assertThat(exception.getMessage()).isEqualTo("Manager with Id: " + managerId + " not found.");
    }

    @Test
    void test_updateManager_success() throws SQLException {
        // prepare
        Manager expectedManager = managerService.createNewManager(
                new ManagerCreateDto(
                        "Bob",
                        "Dilan",
                        "bob.smith@example.com",
                        "+123456789"));

        UUID managerId = managerService.findManagerById(expectedManager.getId()).getId();
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
    }

    @Test
    void test_deleteManager_success() throws SQLException {
        // prepare
        Manager expectedManager = managerService.createNewManager(
                new ManagerCreateDto(
                        "Bob",
                        "Dilan",
                        "bob.smith@example.com",
                        "+123456789"));

        UUID managerId = managerService.findManagerById(expectedManager.getId()).getId();

        // test
        managerService.deleteManager(managerId);

        // assert
        assertThat(dbUtil.managerExistsByEmail("bob.smith@example.com")).isFalse();
    }

    @Test
    void test_deleteManagerNotExistingManager_exception() {
        // prepare
        UUID managerId = UUID.randomUUID();

        // test
        Exception exception = assertThrows(ManagerNotFoundException.class, () -> {
            managerService.deleteManager(managerId);
        });

        // assert
        assertThat(exception.getMessage()).isEqualTo("Manager with Id: " + managerId + " not found.");
    }

    @Test
    void test_deleteManagerWithNullManagerId_exception() {
        // prepare
        UUID managerId = null;

        // test
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            managerService.deleteManager(managerId);
        });

        // assert
        assertThat(exception.getMessage()).isEqualTo("Manager Id cannot be null.");
    }
}

