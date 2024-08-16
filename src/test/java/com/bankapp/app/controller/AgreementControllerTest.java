package com.bankapp.app.controller;

import com.bankapp.app.dto.AgreementDTO;
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

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/delete_tables.sql")
@Sql("/create_tables.sql")
@Sql("/insert_tables.sql")
@RequiredArgsConstructor
class AgreementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testCreateAgreement() throws Exception {
        AgreementDTO newAgreementDTO = new AgreementDTO();
        newAgreementDTO.setInterestRate(new BigDecimal("0.02"));
        newAgreementDTO.setStatus("ACTIVE");
        newAgreementDTO.setSum(new BigDecimal("1000.00"));
        newAgreementDTO.setProductId("8df40ce4-969c-11ee-b9d1-0242ac120002");
        newAgreementDTO.setAccountId("d7d5866c-969c-11ee-b9d1-0242ac120002");

        String json = objectMapper.writeValueAsString(newAgreementDTO);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/agreements/creates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        assertEquals(201, mvcResult.getResponse().getStatus());

        AgreementDTO returned = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertEquals(returned, newAgreementDTO);

    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testGetById() throws Exception {
        AgreementDTO agreementDTO = new AgreementDTO();
        agreementDTO.setInterestRate(new BigDecimal("0.02"));
        agreementDTO.setStatus("ACTIVE");
        agreementDTO.setSum(new BigDecimal("1000.00"));
        agreementDTO.setProductId("8df40ce4-969c-11ee-b9d1-0242ac120002");
        agreementDTO.setAccountId("d7d5866c-969c-11ee-b9d1-0242ac120002");

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/agreements/find/4e1f1090-969d-11ee-b9d1-0242ac120002"))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        AgreementDTO returned = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        assertEquals(returned, agreementDTO);
    }
}