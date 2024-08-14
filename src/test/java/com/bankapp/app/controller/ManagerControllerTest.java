package com.bankapp.app.controller;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.mapper.ManagerMapper;
import com.bankapp.app.model.Manager;
import com.bankapp.app.model.enums.ManagerStatus;
import com.bankapp.app.service.ManagerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Sql("/delete_tables.sql")
@Sql("/create_tables.sql")
@Sql("/insert_tables.sql")
@RequiredArgsConstructor
@ActiveProfiles("test")
class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private ManagerMapper managerMapper;

    @Autowired
    private Validator validator;

    @Test
    void testSerializationAndDeserialization() throws Exception {
        ManagerDTO originalManager = new ManagerDTO();
        originalManager.setFirstName("Alice");
        originalManager.setLastName("Johnson");
        originalManager.setStatus("ACTIVE");

        String json = objectMapper.writeValueAsString(originalManager);

        ManagerDTO deserializedManager = objectMapper.readValue(json, ManagerDTO.class);

        assertEquals(originalManager.getFirstName(), deserializedManager.getFirstName());
        assertEquals(originalManager.getLastName(), deserializedManager.getLastName());
        assertEquals(originalManager.getStatus(), deserializedManager.getStatus());
    }

    @Test
    void testManagerToDtoMapping() {
        Manager manager = new Manager();
        manager.setFirstName("Alice");
        manager.setLastName("Johnson");
        manager.setStatus(ManagerStatus.valueOf("ACTIVE"));

        ManagerDTO dto = managerMapper.toDto(manager);

        assertEquals("Alice", dto.getFirstName());
        assertEquals("Johnson", dto.getLastName());
        assertEquals("ACTIVE", dto.getStatus());
    }

    @Test
    void shouldCreateManagers() throws Exception {
        ManagerDTO create = new ManagerDTO();
        create.setFirstName("Alice");
        create.setLastName("Johnson");
        create.setStatus("ACTIVE");

        String managerDTOStr = objectMapper.writeValueAsString(create);

        String managerDTOPost = mockMvc.
                perform(MockMvcRequestBuilders.post("/managers/creates")
                        .content(managerDTOStr) //
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(managerDTOStr))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(managerDTOPost);
    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void getById() throws Exception {

        ManagerDTO expectancy = new ManagerDTO();
        expectancy.setFirstName("Alice");
        expectancy.setLastName("Johnson");
        expectancy.setStatus("ACTIVE");

        MvcResult mvcResult = mockMvc.
                perform(MockMvcRequestBuilders.
                        get("/managers/find/8d25ab36-969c-11ee-b9d1-0242ac120002"))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        ManagerDTO returned = objectMapper.readValue
                (mvcResult.getResponse()
                                .getContentAsString(),
                        new TypeReference<>() {
                        });
        assertEquals(returned, expectancy);
    }

    @Test
    void testValidRegistrationRequest() throws Exception {
        ManagerDTO expectancy = new ManagerDTO();
        expectancy.setFirstName("Alice");
        expectancy.setLastName("Johnson");
        expectancy.setStatus("ACTIVE");
        Set<ConstraintViolation<ManagerDTO>> constraintViolations =
                validator.validate(expectancy);
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<ManagerDTO> constraintViolation : constraintViolations) {
                System.out.println(constraintViolation.getMessage());
            }
        }
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    void testEmptyManagerFirstName() {
        ManagerDTO expectancy = new ManagerDTO();
        expectancy.setFirstName("");
        expectancy.setLastName("Johnson");
        expectancy.setStatus("ACTIVE");

        Set<ConstraintViolation<ManagerDTO>> constraintViolations =
                validator.validate(expectancy);
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<ManagerDTO> constraintViolation : constraintViolations) {
                System.out.println(constraintViolation.getMessageTemplate() + ": "
                        + constraintViolation.getMessage());
            }
        }
        assertEquals(2, constraintViolations.size());

        Set<String> message = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        assertTrue(message.contains("Can not be empty"),
                "Expected 'Can not be empty', message");
        assertTrue(message.contains("First name must contain only letters"),
                "Expected 'First name must contain only letters', message");
    }

    @Test
    void testInvalidManagerLastName() {
        ManagerDTO expectancy = new ManagerDTO();
        expectancy.setFirstName("Alice");
        expectancy.setLastName("");
        expectancy.setStatus("ACTIVE");

        Set<ConstraintViolation<ManagerDTO>> constraintViolations =
                validator.validate(expectancy);
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<ManagerDTO> constraintViolation : constraintViolations) {
                System.out.println(constraintViolation.getPropertyPath() + ": " +
                        constraintViolation.getMessage());
            }
        }

        assertEquals(2, constraintViolations.size());

        Set<String> message = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
        assertTrue(message.contains("Can not be empty"),
                "Expected 'Can not be empty', message");
        assertTrue(message.contains("Last name must contain only letters"),
                "Expected 'Last name must contain only letters', message");
    }
}

