package com.bankapp.app.controller;

import com.bankapp.app.dto.ClientDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void createClient() throws Exception {

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
        MvcResult mvcResult = mockMvc.
                perform(MockMvcRequestBuilders.post("/clients/creates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        assertEquals(201, mvcResult.getResponse().getStatus());


        ClientDTO returnedClientDto = objectMapper.
                readValue(mvcResult.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });

        assertEquals(returnedClientDto, newClientDto);
    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void getById() throws Exception {
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
                        new TypeReference<>() {});

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
                new TypeReference<>() {});

        assertEquals(returnedDto, clientDto);
    }





}

