package com.bankapp.app.controller;

import com.bankapp.app.dto.AccountDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/delete_tables.sql")
@Sql("/create_tables.sql")
@Sql("/insert_tables.sql")
@RequiredArgsConstructor
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testCreateAccount() throws Exception {
        AccountDTO newAccount = new AccountDTO();
        newAccount.setName("Alice");
        newAccount.setType("CHECKING_ACCOUNT");
        newAccount.setStatus("ACTIVE");
        newAccount.setCurrencyCode("USD");
        newAccount.setBalance(new BigDecimal ("5000.00"));
        newAccount.setClientId("b3a3a896-969c-11ee-b9d1-0242ac120002");

        String json = objectMapper.writeValueAsString(newAccount);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/accounts/creates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        assertEquals(201, mvcResult.getResponse().getStatus());

        AccountDTO returned = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertEquals(returned, newAccount);
    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testGetById() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setName("Alice");
        accountDTO.setType("CHECKING_ACCOUNT");
        accountDTO.setStatus("ACTIVE");
        accountDTO.setCurrencyCode("USD");
        accountDTO.setBalance(new BigDecimal ("5000.00"));
        accountDTO.setClientId("b3a3a896-969c-11ee-b9d1-0242ac120002");

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/accounts/find/d7d5866c-969c-11ee-b9d1-0242ac120002"))
                .andReturn();

        assertEquals(HttpStatus.CREATED, mvcResult.getResponse().getStatus());

        AccountDTO returned = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        assertEquals(returned, accountDTO);
    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testUpdateAccount() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setName("Tom");
        accountDTO.setType("CHECKING_ACCOUNT");
        accountDTO.setStatus("ACTIVE");
        accountDTO.setCurrencyCode("USD");
        accountDTO.setBalance(new BigDecimal ("5000.00"));
        accountDTO.setClientId("b3a3a896-969c-11ee-b9d1-0242ac120002");

        String json = objectMapper.writeValueAsString(accountDTO);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/accounts/update/d7d5866c-969c-11ee-b9d1-0242ac120002")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        AccountDTO returned = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        assertEquals(returned, accountDTO);
    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testSoftDeleteAccount() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setName("Alice");
        accountDTO.setType("CHECKING_ACCOUNT");
        accountDTO.setStatus("DELETED");
        accountDTO.setCurrencyCode("USD");
        accountDTO.setBalance(new BigDecimal ("5000.00"));
        accountDTO.setClientId("b3a3a896-969c-11ee-b9d1-0242ac120002");
        String json = objectMapper.writeValueAsString(accountDTO);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/accounts/delete/d7d5866c-969c-11ee-b9d1-0242ac120002")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        AccountDTO returned = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertEquals(returned, accountDTO);
    }
}