package com.bankapp.app.controller;

import com.bankapp.app.dto.CardDTO;
import com.bankapp.app.generator.CardGenerator;
import com.bankapp.app.model.enums.PaymentSystem;
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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/delete_tables.sql")
@Sql("/create_tables.sql")
@Sql("/insert_tables.sql")
@RequiredArgsConstructor
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testCreateCard() throws Exception {
        CardDTO newCard = new CardDTO();
        newCard.setNumber("4484936489559910");
        newCard.setExpirationDate("2029-08-17");
        newCard.setCardType("CREDIT_CARDS");
        newCard.setPaymentSystem("VISA");
        newCard.setStatus("ACTIVE");
        newCard.setCvv("319");
        newCard.setHolder("Alice Johnson");
        newCard.setClientId("b3a3a896-969c-11ee-b9d1-0242ac120002");
        newCard.setAccountId("d7d5866c-969c-11ee-b9d1-0242ac120002");

        String json = objectMapper.writeValueAsString(newCard);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/cards/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        assertEquals(201, mvcResult.getResponse().getStatus());

        CardDTO returned = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertEquals(returned, newCard);
    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testGetById() throws Exception {

        CardDTO cardDTO = new CardDTO();
        cardDTO.setNumber("1234-5678-9012-3456");
        cardDTO.setExpirationDate("2023-12-31");
        cardDTO.setCardType("CREDIT_CARDS");
        cardDTO.setPaymentSystem("VISA");
        cardDTO.setStatus("ACTIVE");
        cardDTO.setCvv(null);
        cardDTO.setHolder("Alice Johnson");
        cardDTO.setClientId("b3a3a896-969c-11ee-b9d1-0242ac120002");
        cardDTO.setAccountId("d7d5866c-969c-11ee-b9d1-0242ac120002");

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/cards/find/7f580996-969d-11ee-b9d1-0242ac120002")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        CardDTO returned = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        assertEquals(returned, cardDTO);
    }
    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testGenerateCardNumber() {
        String visaNumber = CardGenerator.generateCardNumber(PaymentSystem.VISA);
        String masterCardNumber = CardGenerator.generateCardNumber(PaymentSystem.MASTERCARD);
        String paypalNumber = CardGenerator.generateCardNumber(PaymentSystem.PAYPAL);
        String applePayNumber = CardGenerator.generateCardNumber(PaymentSystem.APPLE_PAY);
        String googlePayNumber = CardGenerator.generateCardNumber(PaymentSystem.GOOGLE_PAY);
        String sepaNumber = CardGenerator.generateCardNumber(PaymentSystem.SEPA);

        assertNotNull(visaNumber);
        assertTrue(visaNumber.matches("^4[0-9]{12}(?:[0-9]{3})?$"));

        assertNotNull(masterCardNumber);
        assertTrue(masterCardNumber.matches("^5[0-9]{15}$"));

        assertNotNull(paypalNumber);
        assertTrue(paypalNumber.matches("^6[0-9]{15}$"));

        assertNotNull(applePayNumber);
        assertTrue(applePayNumber.matches("^7[0-9]{15}$"));

        assertNotNull(googlePayNumber);
        assertTrue(googlePayNumber.matches("^8[0-9]{15}$"));

        assertNotNull(sepaNumber);
        assertTrue(sepaNumber.matches("^9[0-9]{15}$"));
    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testGenerateCardExpirationDate() {
        LocalDate expirationDate = CardGenerator.generateCardExpirationDate();
        LocalDate today = LocalDate.now();

        assertNotNull(expirationDate);
        assertTrue(expirationDate.isAfter(today));
        assertTrue(expirationDate.isBefore(today.plusYears(10)));
    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testGenerateCardCVV() {
        String cvv = CardGenerator.generateCardCVV();
        assertNotNull(cvv);
        assertTrue(cvv.matches("^[0-9]{3}$"));
    }
}