package com.bankapp.app.controller;

import com.bankapp.app.dto.TransactionDTO;
import com.bankapp.app.exception.AccountNotFoundException;
import com.bankapp.app.model.Account;
import com.bankapp.app.repository.AccountRepository;
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

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/delete_tables.sql")
@Sql("/create_tables.sql")
@Sql("/insert_tables.sql")
@RequiredArgsConstructor
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;


    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testCreateTransaction() throws Exception {
        TransactionDTO newTransaction = new TransactionDTO();
        newTransaction.setType("DEPOSIT");
        newTransaction.setAmount(new BigDecimal("1000.00"));
        newTransaction.setCurrencyCode("USD");
        newTransaction.setDescription("Initial deposit");
        newTransaction.setDebitAccount("06c8dc62-969d-11ee-b9d1-0242ac120002");
        newTransaction.setCreditAccount("d7d5866c-969c-11ee-b9d1-0242ac120002");

        String json = objectMapper.writeValueAsString(newTransaction);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/transactions/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        TransactionDTO returned = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        assertEquals(returned, newTransaction);

    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testGetById() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setType("DEPOSIT");
        transactionDTO.setAmount(new BigDecimal("1000.00"));
        transactionDTO.setCurrencyCode("USD");
        transactionDTO.setDescription("Initial deposit");
        transactionDTO.setDebitAccount("06c8dc62-969d-11ee-b9d1-0242ac120002");
        transactionDTO.setCreditAccount("d7d5866c-969c-11ee-b9d1-0242ac120002");


        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/transactions/find/a80e7834-969d-11ee-b9d1-0242ac120002")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        TransactionDTO returned = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        assertEquals(returned, transactionDTO);

    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testDeleteTransactionById() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setType("DELETED");
        transactionDTO.setAmount(new BigDecimal("1000.00"));
        transactionDTO.setCurrencyCode("USD");
        transactionDTO.setDescription("Initial deposit");
        transactionDTO.setDebitAccount("06c8dc62-969d-11ee-b9d1-0242ac120002");
        transactionDTO.setCreditAccount("d7d5866c-969c-11ee-b9d1-0242ac120002");

        MvcResult resultBeforeDeletion = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/transactions/find/a80e7834-969d-11ee-b9d1-0242ac120002")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, resultBeforeDeletion.getResponse().getStatus());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/transactions/delete/a80e7834-969d-11ee-b9d1-0242ac120002")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        TransactionDTO returned = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        assertEquals("DELETED", returned.getType());
        assertEquals(new BigDecimal("1000.00"), returned.getAmount());
        assertEquals("USD", returned.getCurrencyCode());
        assertEquals("Initial deposit", returned.getDescription());
        assertEquals("06c8dc62-969d-11ee-b9d1-0242ac120002", returned.getDebitAccount());
        assertEquals("d7d5866c-969c-11ee-b9d1-0242ac120002", returned.getCreditAccount());

        Account debitAccount = accountRepository.findById(UUID.fromString("06c8dc62-969d-11ee-b9d1-0242ac120002"))
                .orElseThrow(() -> new AccountNotFoundException("Debit account not found"));
        Account creditAccount = accountRepository.findById(UUID.fromString("d7d5866c-969c-11ee-b9d1-0242ac120002"))
                .orElseThrow(() -> new AccountNotFoundException("Credit account not found"));

        assertEquals(new BigDecimal("3000.00"), debitAccount.getBalance());
        assertEquals(new BigDecimal("4000.00"), creditAccount.getBalance());
    }

}
