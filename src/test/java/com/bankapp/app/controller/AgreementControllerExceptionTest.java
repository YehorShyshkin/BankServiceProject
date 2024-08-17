package com.bankapp.app.controller;

import com.bankapp.app.dto.AgreementDTO;
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
class AgreementControllerExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testAgreementNotFoundException() throws Exception {
        UUID agreementId =
                UUID.randomUUID();

        String json = objectMapper.writeValueAsString(agreementId);

        String errorDataJson = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/agreements/find/"+agreementId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorData errorData =
                objectMapper.readValue(errorDataJson, ErrorData.class);

        String expectedMessage =
                String.format("Agreement with id %s not found", agreementId);

        assertEquals(expectedMessage, errorData.message());
    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testCreateAgreementException() throws Exception {

        String productId = UUID.randomUUID().toString();
        String accountId = UUID.randomUUID().toString();
        AgreementDTO agreementDTO = new AgreementDTO();
        agreementDTO.setProductId(productId);
        agreementDTO.setAccountId(accountId);

        String json = objectMapper.writeValueAsString(agreementDTO);

        String errorDataJson = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/agreements/creates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorData errorData = objectMapper.readValue(errorDataJson, ErrorData.class);

        String expectedMessage = String.format("Product with id %s not found", agreementDTO.getProductId());
        assertEquals(expectedMessage, errorData.message());

    }
}
