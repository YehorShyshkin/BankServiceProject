package com.bankapp.app.controller;

import com.bankapp.app.dto.ClientDTO;
import com.bankapp.app.generator.EmailDomainValidator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/delete_tables.sql")
@Sql("/create_tables.sql")
@Sql("/insert_tables.sql")
@RequiredArgsConstructor
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    private EmailDomainValidator emailDomainValidator;

    private ConstraintValidatorContext constraintValidatorContext;


    @Test
    void testCreateClient() throws Exception {

        ClientDTO newClientDto = new ClientDTO();
        newClientDto.setFirstName("Alice");
        newClientDto.setLastName("Johnson");
        newClientDto.setStatus("ACTIVE");
        newClientDto.setEmail("alice@gmail.com");
        newClientDto.setAddress("123 Main St.");
        newClientDto.setPhoneNumber("123-456-7890");
        newClientDto.setTaxCode("1234567890");
        newClientDto.setManagerId(UUID.fromString("8d25ab36-969c-11ee-b9d1-0242ac120002"));


        String json = objectMapper.writeValueAsString(newClientDto);
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/clients/creates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        assertEquals(201, mvcResult.getResponse().getStatus());


        ClientDTO returned = objectMapper.
                readValue(mvcResult.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });

        assertEquals(returned, newClientDto);
    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testGetById() throws Exception {
        ClientDTO clientDto = new ClientDTO();
        clientDto.setFirstName("Alice");
        clientDto.setLastName("Johnson");
        clientDto.setStatus("ACTIVE");
        clientDto.setEmail("alice@gmail.com");
        clientDto.setAddress("123 Main St");
        clientDto.setPhoneNumber("123-456-7890");
        clientDto.setTaxCode("1234567890");
        clientDto.setManagerId(UUID.fromString("8d25ab36-969c-11ee-b9d1-0242ac120002"));

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/clients/find/b3a3a896-969c-11ee-b9d1-0242ac120002"))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        ClientDTO returned = objectMapper.readValue
                (mvcResult.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });

        assertEquals(returned, clientDto);
    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void updateClient() throws Exception {
        ClientDTO clientDto = new ClientDTO();
        clientDto.setFirstName("Alic");
        clientDto.setLastName("Johns");
        clientDto.setStatus("PREMIUM");
        clientDto.setEmail("alice@gmail.com");
        clientDto.setAddress("123 Main St .");
        clientDto.setPhoneNumber("123-456-7891");
        clientDto.setTaxCode("1234567891");
        clientDto.setManagerId(UUID.fromString("8d25ab36-969c-11ee-b9d1-0242ac120002"));

        String json = objectMapper.writeValueAsString(clientDto);

        MvcResult mvcResult = mockMvc.
                perform(MockMvcRequestBuilders
                        .get("/clients/update/" +
                                "b3a3a896-969c-11ee-b9d1-0242ac120002")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        ClientDTO returnedDto = objectMapper.readValue(mvcResult
                        .getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        assertEquals(returnedDto, clientDto);
    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testSoftDeleteClient() throws Exception {
        ClientDTO clientDto = new ClientDTO();
        clientDto.setFirstName("Alice");
        clientDto.setLastName("Johnson");
        clientDto.setStatus("DELETED");
        clientDto.setEmail("alice@gmail.com");
        clientDto.setAddress("123 Main St");
        clientDto.setPhoneNumber("123-456-7890");
        clientDto.setTaxCode("1234567890");
        clientDto.setManagerId(UUID.fromString("8d25ab36-969c-11ee-b9d1-0242ac120002"));

        String json = objectMapper.writeValueAsString(clientDto);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/clients/delete/b3a3a896-969c-11ee-b9d1-0242ac120002")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        ClientDTO returnedDto = objectMapper.readValue(mvcResult
                        .getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        assertEquals(returnedDto, clientDto);

    }

    @Test
    void testValidRegistrationRequest() {
        ClientDTO clientDto = new ClientDTO();
        clientDto.setFirstName("Alice");
        clientDto.setLastName("Johnson");
        clientDto.setStatus("ACTIVE");
        clientDto.setEmail("alice@gmail.com");
        clientDto.setAddress("123 Main St");
        clientDto.setPhoneNumber("123-456-7890");
        clientDto.setTaxCode("1234567890");

        Set<ConstraintViolation<ClientDTO>> constraintViolations =
                validator.validate(clientDto);
        if (!constraintViolations.isEmpty()) {
            constraintViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
        }

        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    void testEmptyClientFirstName() {
        ClientDTO clientDto = new ClientDTO();
        clientDto.setFirstName("");
        clientDto.setLastName("Johnson");
        clientDto.setStatus("ACTIVE");
        clientDto.setEmail("alice@gmail.com");
        clientDto.setAddress("123 Main St");
        clientDto.setPhoneNumber("123-456-7890");
        clientDto.setTaxCode("1234567890");

        Set<ConstraintViolation<ClientDTO>> constraintViolations =
                validator.validate(clientDto);
        if (!constraintViolations.isEmpty()) {
            constraintViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
        }
        assertEquals(2, constraintViolations.size());

        Set<String> message = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        assertTrue(message.contains("Can not be empty"),
                "Expected 'Can not be empty' message");
        assertTrue(message.contains("First name must contain only letters"),
                "Expected 'First name must contain only letters' message");
        // FIXME it would be good to assert log messages for all tests
    }

    @Test
    void testInvalidClientFirstNameNumbers() {
        ClientDTO clientDto = new ClientDTO();
        clientDto.setFirstName("1234");
        clientDto.setLastName("Johnson");
        clientDto.setStatus("ACTIVE");
        clientDto.setEmail("alice@gmail.com");
        clientDto.setAddress("123 Main St");
        clientDto.setPhoneNumber("123-456-7890");
        clientDto.setTaxCode("1234567890");

        Set<ConstraintViolation<ClientDTO>> constraintViolations =
                validator.validate(clientDto);
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
    void testInvalidClientLastName() {
        ClientDTO clientDto = new ClientDTO();
        clientDto.setFirstName("Alice");
        clientDto.setLastName(" ");
        clientDto.setStatus("ACTIVE");
        clientDto.setEmail("alice@gmail.com");
        clientDto.setAddress("123 Main St");
        clientDto.setPhoneNumber("123-456-7890");
        clientDto.setTaxCode("1234567890");

        Set<ConstraintViolation<ClientDTO>> constraintViolations =
                validator.validate(clientDto);
        if (!constraintViolations.isEmpty()) {
            constraintViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
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
    void testInvalidClientLastNameNumbers() {
        ClientDTO clientDto = new ClientDTO();
        clientDto.setFirstName("Alice");
        clientDto.setLastName("12344");
        clientDto.setStatus("ACTIVE");
        clientDto.setEmail("alice@gmail.com");
        clientDto.setAddress("123 Main St");
        clientDto.setPhoneNumber("123-456-7890");
        clientDto.setTaxCode("1234567890");

        Set<ConstraintViolation<ClientDTO>> constraintViolations =
                validator.validate(clientDto);
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

    @Test
    void testInvalidClientEmail() {
        ClientDTO clientDto = new ClientDTO();
        clientDto.setFirstName("Alice");
        clientDto.setLastName("Johnson");
        clientDto.setStatus("ACTIVE");
        clientDto.setEmail("");
        clientDto.setAddress("123 Main St");
        clientDto.setPhoneNumber("123-456-7890");
        clientDto.setTaxCode("1234567890");

        Set<ConstraintViolation<ClientDTO>> constraintViolations =
                validator.validate(clientDto);
        if (!constraintViolations.isEmpty()) {
            constraintViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
        }
        assertEquals(2, constraintViolations.size());

        Set<String> message = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        assertTrue(message.contains("Email cannot be blank"),
                "Expected 'Email cannot be blank' message");
        assertTrue(message.contains("Invalid email domain"),
                "Expected 'Invalid email domain' message");

    }

    @BeforeEach
    void setUp() {
        emailDomainValidator = new EmailDomainValidator();
    }

    @Test
    void testValidEmail() {
        assertTrue(emailDomainValidator.isValid("user@gmail.com", constraintValidatorContext),
                "Expected valid email with allowed domain");
        assertTrue(emailDomainValidator.isValid("example@proton.me", constraintValidatorContext),
                "Expected valid email with allowed domain");
    }

    @Test
    void testEmailWithoutAtSymbol() {
        assertFalse(emailDomainValidator.isValid("usergmail.com", null),
                "Expected invalid email without '@'");
        assertFalse(emailDomainValidator.isValid("example.proton.me", null),
                "Expected invalid email without '@'");
    }

    @Test
    void testInvalidDomain() {
        assertFalse(emailDomainValidator.isValid("user@invalid.com", null),
                "Expected invalid email with not allowed domain");
        assertFalse(emailDomainValidator.isValid("example@unknown.net", null),
                "Expected invalid email with not allowed domain");
    }

    @Test
    void testEmptyEmail() {
        assertFalse(emailDomainValidator.isValid("", null),
                "Expected invalid for empty email");
        assertFalse(emailDomainValidator.isValid(null, null),
                "Expected invalid for null email");
    }

    @Test
    void testEmailWithOnlyLocalPart() {
        assertFalse(emailDomainValidator.isValid("user@", null),
                "Expected invalid email with only local part");
    }

    @Test
    void testEmailWithOnlyDomain() {
        assertFalse(emailDomainValidator.isValid("@gmail.com", null),
                "Expected invalid email with only domain");
    }
}

