package com.bankapp.app.controller;

import com.bankapp.app.dto.AccountDTO;
import com.bankapp.app.request.ErrorData;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/delete_tables.sql")
@Sql("/create_tables.sql")
@Sql("/insert_tables.sql")
@RequiredArgsConstructor
class AccountControllerExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testAccountNotFoundException() throws Exception {
        UUID accountId =
                UUID.randomUUID();

        String json = objectMapper.writeValueAsString(accountId);

        String errorDataJson = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/accounts/find/" + accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorData errorData =
                objectMapper.readValue(errorDataJson, ErrorData.class);

        String expectedMessage =
                String.format("Account with id %s not found", accountId);

        assertEquals(expectedMessage, errorData.message());
    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testCreateAccountException() throws Exception {
        String clientId = UUID.randomUUID().toString();

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setClientId(clientId);

        String json = objectMapper.writeValueAsString(accountDTO);

        String errorDataJson = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/accounts/creates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorData errorData = objectMapper.readValue(errorDataJson, ErrorData.class);

        String expectedMessage =
                "Client with id " + clientId + " not found";

        assertEquals(expectedMessage, errorData.message());
    }

}
