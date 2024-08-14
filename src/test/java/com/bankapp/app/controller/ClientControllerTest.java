package com.bankapp.app.controller;

import com.bankapp.app.dto.ClientDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

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
    void findAll() throws Exception {

        ClientDTO clientDTOFirst = new ClientDTO();
        clientDTOFirst.setFirstName("Alice");
        clientDTOFirst.setLastName("Johnson");
        clientDTOFirst.setStatus("ACTIVE");
        clientDTOFirst.setEmail("alice@gmail.com");
        clientDTOFirst.setAddress("123 Main St");
        clientDTOFirst.setPhoneNumber("123-456-7890");
        clientDTOFirst.setTaxCode("123456789");


        ClientDTO clientDTOSecond = new ClientDTO();
        clientDTOSecond.setFirstName("Bob");
        clientDTOSecond.setLastName("Smith");
        clientDTOSecond.setStatus("ACTIVE");
        clientDTOSecond.setEmail("bob@gmail.com");
        clientDTOSecond.setAddress("456 Oak St");
        clientDTOSecond.setPhoneNumber("456-789-0123");
        clientDTOSecond.setTaxCode("987654321");

        List<ClientDTO> clientDTOList = new ArrayList<>();
        clientDTOList.add(clientDTOFirst);
        clientDTOList.add(clientDTOSecond);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/clients/all"))
                .andReturn();
        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());

        List<ClientDTO> clientDTOReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        Assertions.assertEquals(clientDTOList, clientDTOReturned);

    }


    @Test
    void getClientDTO() throws Exception {
        ClientDTO clientDTOFind = new ClientDTO();
        clientDTOFind.setFirstName("Alice");
        clientDTOFind.setLastName("Johnson");
        clientDTOFind.setStatus("ACTIVE");
        clientDTOFind.setEmail("alice@gmail.com");
        clientDTOFind.setAddress("123 Main St");
        clientDTOFind.setPhoneNumber("123-456-7890");
        clientDTOFind.setTaxCode("123456789");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/clients/find/b3a3a896-969c-11ee-b9d1-0242ac120002"))
                .andReturn();

        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
        ClientDTO clientDTOReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ClientDTO.class);
        Assertions.assertEquals(clientDTOFind, clientDTOReturned);


    }

    @Test
    void createClient() throws Exception {

        ClientDTO clientDTOCreate = new ClientDTO();
        clientDTOCreate.setFirstName("Alice");
        clientDTOCreate.setLastName("Johnson");
        clientDTOCreate.setStatus("ACTIVE");
        clientDTOCreate.setEmail("alice@gmail.com");
        clientDTOCreate.setAddress("123 Main St");
        clientDTOCreate.setPhoneNumber("123-456-7890");
        clientDTOCreate.setTaxCode("123456789");


        String clientDTOStr = objectMapper.writeValueAsString(clientDTOCreate);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/clients/create_clients/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientDTOStr))
                .andReturn();

        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());


        ClientDTO clientDTOReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertEquals(clientDTOReturned, clientDTOCreate);
    }
}

