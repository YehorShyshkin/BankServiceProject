package com.bankapp.app.controller;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.entity.Manager;
import com.bankapp.app.enums.ManagerStatus;
import com.bankapp.app.service.ManagerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
    public void shouldCreateManagers() {


        Manager thisManager = new Manager();
        thisManager.setManagerFirstName("John");
        thisManager.setManagerLastName("Doe");
        thisManager.setManagerStatus(ManagerStatus.valueOf("ACTIVE"));
        thisManager.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        thisManager.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        Manager createManager = managerService.save(thisManager);
        ManagerDTO expectedManager = new ManagerDTO();
        expectedManager.setManagerLastName("Doe");
        expectedManager.setManagerStatus(String.valueOf(ManagerStatus.ACTIVE));
        assertEquals(expectedManager.getManagerLastName(), createManager.getManagerLastName());
        assertEquals(expectedManager.getManagerStatus(), createManager.getManagerStatus().toString());

    }

}
