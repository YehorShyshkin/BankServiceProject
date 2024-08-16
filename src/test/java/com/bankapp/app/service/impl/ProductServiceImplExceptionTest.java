package com.bankapp.app.service.impl;

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
class ProductServiceImplExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testProductNotFoundException() throws Exception {
        UUID productId =
                UUID.randomUUID();

        String json =
                objectMapper.writeValueAsString(productId);

        String errorDataJson = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/products/find/"+productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorData errorData =
                objectMapper.readValue(errorDataJson, ErrorData.class);

        String expectedMessage =
                String.format("Product with id %s not found", productId);

        assertEquals(expectedMessage, errorData.message());
    }

}
