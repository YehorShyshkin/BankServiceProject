package com.bankapp.app.controller;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.service.ManagerService;
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


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureMockMvc
@Sql("/delete_tables.sql")
@Sql("/create_tables.sql")
@Sql("/insert_tables.sql")
@RequiredArgsConstructor
class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ManagerService managerService;

    @Test
    public void shouldCreateManagers() throws Exception {

        ManagerDTO managerDTOCreate = new ManagerDTO();
        managerDTOCreate.setManagerLastName("Doe");
        managerDTOCreate.setManagerStatus("ACTIVE");

        /**
         * ----- Russian ------
         * Преобразование объекта managerDTOReturned в JSON.
         * <p>
         * ----- English -------
         * Convert managerDTOReturned object to JSON.
         */
        String managerDTOStr = objectMapper.writeValueAsString(managerDTOCreate);

        /**
         * ----- Russian ------
         * Отправка POST-запроса к контроллеру.
         * <p>
         * ----- English -------
         * Sending a POST request to the controller.
         */
        MvcResult managerDTOPost = mockMvc.perform(MockMvcRequestBuilders.post("/managers/create_managers/")
                        .content(managerDTOStr) //
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        /**
         * ----- Russian ------
         * Проверка статуса ответа.
         * После выполнения запроса, тест проверяет, что HTTP-ответ имеет статус 200 (OK).
         * <p>
         * ----- English -------
         * Checking the response status.
         * After executing the request, the test checks that the HTTP response has a status of 200 (OK).
         */
        assertEquals(200, managerDTOPost.getResponse().getStatus());

        /**
         * ----- Russian ------
         * Чтение JSON-ответа и сравнение с ожидаемым managerDTOReturned.
         * JSON-ответ, полученный из managerDTOPost, преобразуется обратно в объект managerDTOReturned
         * с использованием objectMapper.readValue.
         * <p>
         * ----- English -------
         *  Read the JSON response and compare it to the expected managerDTOReturned.
         *  The JSON response received from managerDTOPost is converted back to a managerDTOReturned object
         *  using objectMapper.readValue.
         */
        ManagerDTO managerDTOReturned = objectMapper.readValue(managerDTOPost
                        .getResponse()
                        .getContentAsString(),
                new TypeReference<>() {
                });


        /**
         * ----- Russian ------
         * Сравнение полей возвращенного объекта (managerDTOReturned) с полями изначально созданного объекта (managerDTOCreate).
         * Если все поля совпадают, то тест считается успешным.
         * <p>
         * ---- English -------
         * Compare the fields of the returned object (managerDTOReturned) with the fields of the originally created object (managerDTOCreate).
         * If all fields match, then the test is considered successful
         */
        Assertions.assertEquals(managerDTOReturned, managerDTOCreate);
    }

    @Test
    public void shouldFindManagerDTOByIdTest() throws Exception {
        // given
        ManagerDTO findManagerDTO = new ManagerDTO();
        findManagerDTO.setManagerLastName("Doe");
        findManagerDTO.setManagerStatus("ACTIVE");

        // when
        MvcResult managerDTOGet = mockMvc.perform(MockMvcRequestBuilders.get("/managers/find/6e42ea78-969c-11ee-b9d1-0242ac120002"))
                .andReturn();
        Assertions.assertEquals(200, managerDTOGet.getResponse().getStatus());

        // then
        ManagerDTO retunManagerDTO = objectMapper.readValue(managerDTOGet.getResponse().getContentAsString(), new TypeReference<>() {
        });
        Assertions.assertEquals(retunManagerDTO, findManagerDTO);
    }

    @Test
    public void shouldAllManager() throws Exception {

        ManagerDTO managerDTOFirst = new ManagerDTO();
        managerDTOFirst.setManagerLastName("Doe");
        managerDTOFirst.setManagerStatus("ACTIVE");

        ManagerDTO managerDTOSecond = new ManagerDTO();
        managerDTOSecond.setManagerLastName("Smith");
        managerDTOSecond.setManagerStatus("INACTIVE");

        List<ManagerDTO> managerDTOList = new ArrayList<>();
        managerDTOList.add(managerDTOFirst);
        managerDTOList.add(managerDTOSecond);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/managers/all"))
                .andReturn();
        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());

        List<ManagerDTO> managerDTOReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertEquals(managerDTOList, managerDTOReturned);
    }


    @Test
    public void shouldDeleteManager() throws Exception {

        // given
//        ManagerDTO managerDTO = new ManagerDTO();
//        managerDTO.setManagerLastName("Smith");
//        managerDTO.setManagerStatus("DELETED");

        // when

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/managers/delete_manager/7e964ba4-969c-11ee-b9d1-0242ac120002"))
                .andReturn();

        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());

        // then

        String responseContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Manager deleted successfully!", responseContent);

    }

    @Test
    void shouldUpdateManager() throws Exception {
        ManagerDTO managerDTOUpdate = new ManagerDTO();
        managerDTOUpdate.setManagerLastName("Lion");
        managerDTOUpdate.setManagerStatus("ON_LEAVE");

        String managerDTOJson = objectMapper.writeValueAsString(managerDTOUpdate);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/managers/update_manager/7e964ba4-969c-11ee-b9d1-0242ac120002")
                        .content(managerDTOJson) //
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
        ManagerDTO updatedManagerDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        Assertions.assertEquals(managerDTOUpdate, updatedManagerDTO);
    }

    @Test
    public void mergeManagerAndClientShouldFail() throws Exception {
        UUID managerId = UUID.fromString("6e42ea78-969c-11ee-b9d1-0242ac120002");
        UUID clientId = UUID.fromString("b3a3a896-969c-11ee-b9d1-0242ac120002");

        String requestContent = "{\"managerId\":\"" + managerId + "\",\"clientId\":\"" + clientId + "\"}";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/managers/manager_client/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andReturn();
        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
        String responseContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Manager has been successfully added to the client!", responseContent);

    }
}
