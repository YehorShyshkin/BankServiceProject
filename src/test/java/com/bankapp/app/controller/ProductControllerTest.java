package com.bankapp.app.controller;

import com.bankapp.app.dto.ProductDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureMockMvc
@Sql("/delete_tables.sql")
@Sql("/create_tables.sql")
@Sql("/insert_tables.sql")
@RequiredArgsConstructor
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testCreateProduct() throws Exception {
        ProductDTO newProduct = new ProductDTO();
        newProduct.setName("Savings Account");
        newProduct.setStatus("ACTIVE");
        newProduct.setCurrencyCode("USD");
        newProduct.setInterestRate(new BigDecimal("0.02"));
        newProduct.setProductLimit(new BigDecimal("10000"));
        newProduct.setManagerId("8d25ab36-969c-11ee-b9d1-0242ac120002");

        String json = objectMapper.writeValueAsString(newProduct);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/products/creates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        assertEquals(201, mvcResult.getResponse().getStatus());

        ProductDTO returnedProductDto = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(),
                        new TypeReference<>() {});

        assertEquals(returnedProductDto, newProduct);
    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testGetById() throws Exception {
        ProductDTO newProduct = new ProductDTO();
        newProduct.setName("Savings Account");
        newProduct.setStatus("ACTIVE");
        newProduct.setCurrencyCode("USD");
        newProduct.setInterestRate(new BigDecimal("0.02"));
        newProduct.setProductLimit(new BigDecimal("10000"));
        newProduct.setManagerId("8d25ab36-969c-11ee-b9d1-0242ac120002");
// FIXME 2 empty lines

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/products/find/8df40ce4-969c-11ee-b9d1-0242ac120002"))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        ProductDTO returned = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertEquals(returned, newProduct);

    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testUpdateProduct() throws Exception {
        ProductDTO newProduct = new ProductDTO();
        newProduct.setName("Savings");
        newProduct.setStatus("ACTIVE");
        newProduct.setCurrencyCode("EUR");
        newProduct.setInterestRate(new BigDecimal("0.02"));
        newProduct.setProductLimit(new BigDecimal("10000"));
        newProduct.setManagerId("8d25ab36-969c-11ee-b9d1-0242ac120002");

        String json = objectMapper.writeValueAsString(newProduct);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/products/update/8df40ce4-969c-11ee-b9d1-0242ac120002")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        ProductDTO returnedDto = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertEquals(returnedDto, newProduct);
    }

    @Test
    @WithMockUser(username = "aloha.test@gmail.com")
    void testSoftDeleteProduct() throws Exception {
        ProductDTO newProduct = new ProductDTO();
        newProduct.setName("Savings Account");
        newProduct.setStatus("DELETED");
        newProduct.setCurrencyCode("USD");
        newProduct.setInterestRate(new BigDecimal("0.02"));
        newProduct.setProductLimit(new BigDecimal("10000"));
        newProduct.setManagerId("8d25ab36-969c-11ee-b9d1-0242ac120002");

        String json = objectMapper.writeValueAsString(newProduct);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/products/delete/8df40ce4-969c-11ee-b9d1-0242ac120002")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        ProductDTO returnedDto = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertEquals(returnedDto, newProduct);

    }

}