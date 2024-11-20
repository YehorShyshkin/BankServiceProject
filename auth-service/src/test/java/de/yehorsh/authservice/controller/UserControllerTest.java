package de.yehorsh.authservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import de.yehorsh.authservice.AuthServiceApplication;
import de.yehorsh.authservice.config.ContainersEnvironment;
import de.yehorsh.authservice.dto.UserDro;
import de.yehorsh.authservice.model.entity.User;
import de.yehorsh.authservice.repository.UserRepository;
import de.yehorsh.authservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(
        classes = AuthServiceApplication.class
        , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {ContainersEnvironment.Initializer.class})
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DBUtil dbUtil;

    @BeforeEach
    void cleanUpDatabase() {
        dbUtil = new DBUtil(dataSource);
    }

    @Test
    void test_createUser_success() throws Exception {
        // prepare
        createUserAndExpect(
                "BruceWayne",
                "fafe#1af@fAfa_",
                "bruce.wayne@example.com",
                "MAIN_ADMIN",
                201,
                "User created successfully");

        assertThat(dbUtil.userExistsByEmail("bruce.wayne@example.com")).isTrue();

        // test
        User createUser = userRepository.findByEmail("bruce.wayne@example.com").get();

        // assert
        assertThat(createUser.getUserId()).isNotNull();
        assertThat(createUser.getUsername()).isEqualTo("BruceWayne");
        assertThat(passwordEncoder.matches("fafe#1af@fAfa_", createUser.getPassword())).isTrue();
        assertThat(createUser.getEmail()).isEqualTo("bruce.wayne@example.com");
        assertThat(createUser.getRoles()).isEqualTo("MAIN_ADMIN");
        assertThat(createUser.getUpdateDate()).isNotNull();

    }

    private void createUserAndExpect(String userName, String password,  String email, String roleName,
                                        int expectedStatus, String expectedResponse) throws Exception {

        UserDro newUserDto = new UserDro(userName, password, email, roleName);
        String userJson = objectMapper.writeValueAsString(newUserDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/main_admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().string(expectedResponse));
    }
}