package de.yehorsh.managerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.yehorsh.managerservice.ManagerServiceApplication;
import de.yehorsh.managerservice.config.ContainersEnvironment;
import de.yehorsh.managerservice.dto.ManagerCreateDto;
import de.yehorsh.managerservice.dto.ManagerUpdateDto;
import de.yehorsh.managerservice.model.Manager;
import de.yehorsh.managerservice.model.enums.ManagerStatus;
import de.yehorsh.managerservice.repository.ManagerRepository;
import de.yehorsh.managerservice.service.ManagerService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(
        classes = ManagerServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {ContainersEnvironment.Initializer.class})
class ManagerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DBUtil dbUtil;
    @Autowired
    private MeterRegistry meterRegistry;

    @BeforeEach
    void cleanUpDatabase() {
        managerRepository.deleteAll();
        dbUtil = new DBUtil(dataSource);
        meterRegistry.clear();
    }

    @ParameterizedTest
    @CsvSource({
            // Testing: Valid manager details with all valid fields
            "'Bruce', 'Wayne', 'bruce.wayne@example.com', '+1234567891', 201, 'Manager was successfully created!'",
            // Testing: First name is empty
            "'', 'Doe', 'john.doe@example.com', '+123456789', 400, ''",
            // Testing: Last name is empty
            "'John', '', 'john.doe@example.com', '+123456789', 400, ''",
            // Testing: Email is empty
            "'John', 'Doe', '', '+123456789', 400, ''",
            // Testing: Phone number is empty
            "'John', 'Doe', 'john.doe@example.com', '', 400, ''",
            // Testing: Invalid email format
            "'John', 'Doe', 'invalid-email', '+123456789', 400, ''",
            // Testing: Invalid phone number format
            "'John', 'Doe', 'john.doe@example.com', 'invalid-phone', 400, ''"
    })
    void test_createManager_invalidCases(String firstName, String lastName, String email, String phone,
                                         int expectedStatus, String expectedResponse) throws Exception {
        createManagerAndExpect(firstName, lastName, email, phone, expectedStatus, expectedResponse);
    }

    @Test
    void test_createManager_success() throws Exception {
        // prepare
        var requestCounter = meterRegistry.counter("create_manager_endpoint_count");
        double initialCount = requestCounter.count();

        var requestTimer = meterRegistry.timer("create_manager_endpoint_timer");
        long initialTimeCount = requestTimer.count();

        createManagerAndExpect(
                "Bruce",
                "Wayne",
                "bruce.wayne@example.com",
                "+1234567891",
                201,
                "Manager was successfully created!"
        );

        assertThat(dbUtil.managerExistsByEmail("bruce.wayne@example.com")).isTrue();

        // test
        Manager createdManager = managerRepository.findManagerByEmail("bruce.wayne@example.com").get();

        // assert
        assertThat(createdManager.getFirstName()).isEqualTo("Bruce");
        assertThat(createdManager.getLastName()).isEqualTo("Wayne");
        assertThat(createdManager.getEmail()).isEqualTo("bruce.wayne@example.com");
        assertThat(createdManager.getPhoneNumber()).isEqualTo("+1234567891");
        assertThat(createdManager.getId()).isNotNull();
        assertThat(createdManager.getCreationDate()).isNotNull();
        assertThat(createdManager.getUpdateDate()).isNotNull();

        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
        assertThat(requestTimer.count()).isEqualTo(initialTimeCount + 1);
    }

    @Test
    void test_createManagerWithSameLastName_badRequest() throws Exception {
        createManagerAndExpect(
                "John",
                "Weak",
                "john@example.com",
                "+1234567891",
                201,
                "Manager was successfully created!"
        );
        createManagerAndExpect(
                "John",
                "Weak",
                "john.weak@example.com",
                "+123456789000",
                400,
                "{\"httpStatus\":" +
                        "\"BAD_REQUEST\",\"message\":" +
                        "\"Manager with the provided details already exists.\"}"
        );
        assertThat(dbUtil.managerExistsByEmail("john.weak@example.com")).isFalse();
    }

    @Test
    void test_findManager_success() throws Exception {
        // prepare
        Manager expectedManager = managerService.createNewManager(
                new ManagerCreateDto(
                        "John",
                        "Doe",
                        "john.doe@example.com",
                        "+1234567890"));

        var requestCounter = meterRegistry.counter("find_manager_endpoint_count");
        double initialCount = requestCounter.count();

        // test
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/" + expectedManager.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // assert
        assertThat(dbUtil.managerExistsByEmail("john.doe@example.com")).isTrue();
        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
    }

    @Test
    void test_updateManager_success() throws Exception {
        // prepare
        Manager expectedManager = managerService.createNewManager(
                new ManagerCreateDto(
                        "John",
                        "Doe",
                        "john.doe@example.com",
                        "+123456789"));

        ManagerUpdateDto managerUpdateDto = new ManagerUpdateDto(
                expectedManager.getId(),
                "Alisa",
                "First",
                "alisa.first@example.com",
                "+1234567811",
                ManagerStatus.ACTIVE);

        var requestCounter = meterRegistry.counter("update_manager_endpoint_count");
        double initialCount = requestCounter.count();

        String managerJson = objectMapper.writeValueAsString(managerUpdateDto);

        // test
        mockMvc.perform(MockMvcRequestBuilders.put("/managers/" + expectedManager.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(managerJson))
                .andExpect(status().isOk());

        // assert
        assertThat(dbUtil.managerExistsByEmail("alisa.first@example.com")).isTrue();
        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);

        Manager actualManager = managerRepository.findById(expectedManager.getId()).get();

        assertThat(actualManager.getFirstName()).isEqualTo("Alisa");
        assertThat(actualManager.getLastName()).isEqualTo("First");
        assertThat(actualManager.getEmail()).isEqualTo("alisa.first@example.com");
        assertThat(actualManager.getPhoneNumber()).isEqualTo("+1234567811");
        assertThat(actualManager.getManagerStatus()).isEqualTo(ManagerStatus.ACTIVE);
    }

    @Test
    void test_deleteManager_success() throws Exception {
        // prepare
        Manager expectedManager = managerService.createNewManager(
                new ManagerCreateDto(
                        "John",
                        "Doe",
                        "john.doe@example.com",
                        "+123456789"));

        var requestCounter = meterRegistry.counter("delete_manager_endpoint_count");
        double initialCount = requestCounter.count();

        String managerJson = objectMapper.writeValueAsString(expectedManager);

        // test
        mockMvc.perform(MockMvcRequestBuilders.delete("/managers/" + expectedManager.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(managerJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Manager with ID " + expectedManager.getId() + " was deleted!"));

        // assert
        assertThat(dbUtil.managerExistsByEmail("john.doe@example.com")).isFalse();
        assertThat(managerRepository.existsById(expectedManager.getId()))
                .as("Manager should be deleted").isFalse();
        assertThat(requestCounter.count()).isEqualTo(initialCount + 1);
    }

    private void createManagerAndExpect(String firstName, String lastName, String email, String phone,
                                        int expectedStatus, String expectedResponse) throws Exception {

        ManagerCreateDto newManagerDto = new ManagerCreateDto(firstName, lastName, email, phone);
        String managerJson = objectMapper.writeValueAsString(newManagerDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(managerJson))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().string(expectedResponse));
    }
}