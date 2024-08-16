package com.bankapp.app.service.impl;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.mapper.ManagerMapper;
import com.bankapp.app.model.Manager;
import com.bankapp.app.model.enums.ManagerStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
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
@ActiveProfiles("test")
class ManagerServiceImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testShouldCreateManagers() throws Exception {
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
    void testGetById() throws Exception {

        ManagerDTO expectancy = new ManagerDTO();
        expectancy.setFirstName("Henry");
        expectancy.setLastName("Rodriguez");
        expectancy.setStatus("INACTIVE");

        MvcResult mvcResult = mockMvc.
                perform(MockMvcRequestBuilders.
                        get("/managers/find/" +
                                "f0920ab0-969c-11ee-b9d1-0242ac120002"))
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
    @WithMockUser(username = "aloha.test@gmail.com")
    void testUpdateManager() throws Exception {
        ManagerDTO updateDto = new ManagerDTO();
        updateDto.setFirstName("Ali");
        updateDto.setLastName("John");
        updateDto.setStatus("INACTIVE");

        String json = objectMapper.writeValueAsString(updateDto);

        MvcResult mvcResult = mockMvc.
                perform(MockMvcRequestBuilders
                        .get("/managers/update/" +
                                "f647f8b6-969c-11ee-b9d1-0242ac120002")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        ManagerDTO returnedDto = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), new TypeReference<>() {
        });
        assertEquals(returnedDto, updateDto);
    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testSoftDeleteManager() throws Exception {
        ManagerDTO deleteDto = new ManagerDTO();
        deleteDto.setFirstName("Olivia");
        deleteDto.setLastName("White");
        deleteDto.setStatus("DELETED");
        String json = objectMapper.writeValueAsString(deleteDto);

        MvcResult mvcResult = mockMvc.
                perform(MockMvcRequestBuilders
                        .get("/managers/delete/f869b0e2-969c-11ee-b9d1-0242ac120002")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        ManagerDTO returnedDto = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), new TypeReference<>() {
        });

        assertEquals(returnedDto, deleteDto);
    }

    @Test
    void testValidRegistrationRequest() {
        ManagerDTO expectancy = new ManagerDTO();
        expectancy.setFirstName("Alice");
        expectancy.setLastName("Johnson");
        expectancy.setStatus("ACTIVE");
        Set<ConstraintViolation<ManagerDTO>> constraintViolations =
                validator.validate(expectancy);
        if (!constraintViolations.isEmpty()) {
            constraintViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
        }
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    void testInvalidManagerFirstName() {
        ManagerDTO expectancy = new ManagerDTO();
        expectancy.setFirstName("");
        expectancy.setLastName("Johnson");
        expectancy.setStatus("ACTIVE");

        Set<ConstraintViolation<ManagerDTO>> constraintViolations =
                validator.validate(expectancy);
        if (!constraintViolations.isEmpty()) {
            constraintViolations.stream()
                    .map(constraintViolation -> constraintViolation.getMessageTemplate() + ": "
                            + constraintViolation.getMessage())
                    .forEach(System.out::println);
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
    void testInvalidMangerFirstNameNumbers() {
        ManagerDTO expectancy = new ManagerDTO();
        expectancy.setFirstName("1234");
        expectancy.setLastName("Johnson");
        expectancy.setStatus("ACTIVE");

        Set<ConstraintViolation<ManagerDTO>> constraintViolations =
                validator.validate(expectancy);
        if (!constraintViolations.isEmpty()) {
            constraintViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
        }
        assertEquals(1, constraintViolations.size());

        Set<String> message = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
        assertTrue(message.contains("First name must contain only letters"),
                "Expected 'First name must contain only letters' message");
    }

    @Test
    void testInvalidManagerLastName() {
        ManagerDTO expectancy = new ManagerDTO();
        expectancy.setFirstName("Alice");
        expectancy.setLastName("");
        expectancy.setStatus("ACTIVE");

        Set<ConstraintViolation<ManagerDTO>> constraintViolations =
                validator.validate(expectancy);
        if (!constraintViolations.isEmpty()) {
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

    @Test
    void testInvalidManagerLastNameNumbers() {
        ManagerDTO expectancy = new ManagerDTO();
        expectancy.setFirstName("Alice");
        expectancy.setLastName("1234");
        expectancy.setStatus("ACTIVE");
        Set<ConstraintViolation<ManagerDTO>> constraintViolations =
                validator.validate(expectancy);
        if (!constraintViolations.isEmpty()) {
            constraintViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
        }
        assertEquals(1, constraintViolations.size());

        Set<String> message = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        assertTrue(message.contains("Last name must contain only letters"),
                "Expected 'Last name must contain only letters', message");


    }
}

