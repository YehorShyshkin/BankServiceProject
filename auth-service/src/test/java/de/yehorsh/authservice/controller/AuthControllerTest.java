package de.yehorsh.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.yehorsh.authservice.AuthServiceApplication;
import de.yehorsh.authservice.config.ContainersEnvironment;
import de.yehorsh.authservice.dto.JwtAuthenticationDto;
import de.yehorsh.authservice.dto.RefreshTokenDto;
import de.yehorsh.authservice.dto.UserCredentialsDto;
import de.yehorsh.authservice.dto.UserDto;
import de.yehorsh.authservice.model.entity.Role;
import de.yehorsh.authservice.model.enums.RoleName;
import de.yehorsh.authservice.repository.RoleRepository;
import de.yehorsh.authservice.security.jwt.JwtService;
import de.yehorsh.authservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(classes = AuthServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {ContainersEnvironment.Initializer.class})
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DBUtil dbUtil;

    @BeforeEach
    void cleanUpDatabase() {
        dbUtil = new DBUtil(dataSource);
        for (RoleName roleName : RoleName.values()) {
            if (!roleRepository.existsByName(roleName.name())) {
                Role role = new Role();
                role.setId(UUID.randomUUID());
                role.setName(String.valueOf(roleName));
                roleRepository.save(role);
            }
        }
    }

    @Test
    void test_auth_success() throws Exception {
        // prepare
        userService.createUser(
                new UserDto(
                        "bruce@example.com",
                        "fafe#1af@fAfa_",
                        RoleName.MANAGER.toString()
                ), RoleName.MANAGER);

        UserCredentialsDto loginRequest = new UserCredentialsDto();
        loginRequest.setEmail("bruce@example.com");
        loginRequest.setPassword("fafe#1af@fAfa_");

        String userJson = objectMapper.writeValueAsString(loginRequest);

        // test
        String tokenJson = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JwtAuthenticationDto jwtAuthenticationDto = objectMapper.readValue(tokenJson, JwtAuthenticationDto.class);

        // assert
        assertThat(loginRequest.getEmail()).isEqualTo(jwtService.getEmailFromToken(jwtAuthenticationDto.getToken()));
        assertThat(dbUtil.userExistsByEmail("bruce@example.com")).isTrue();
    }

    @Test
    void test_auth_failure() throws Exception {
        // prepare
        UserCredentialsDto loginRequest = new UserCredentialsDto();
        loginRequest.setEmail("bruce.wayne@example.com");
        loginRequest.setPassword("fafe#1af@fAfa_");

        String userJson = objectMapper.writeValueAsString(loginRequest);

        // test
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void test_refresh_success() throws Exception {
        // prepare
        userService.createUser(
                new UserDto(
                        "wayne@example.com",
                        "fafe#1af@fAfa_",
                        RoleName.MANAGER.toString()
                ), RoleName.MANAGER);

        UserCredentialsDto loginRequest = new UserCredentialsDto();
        loginRequest.setEmail("wayne@example.com");
        loginRequest.setPassword("fafe#1af@fAfa_");

        String userJson = objectMapper.writeValueAsString(loginRequest);

        // test
        String tokenJson = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();

        refreshTokenDto.setRefreshToken(objectMapper.readValue(tokenJson, JwtAuthenticationDto.class).getRefreshToken());

        String refreshTokenJson = objectMapper.writeValueAsString(refreshTokenDto);

        // assert
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshTokenJson))
                .andExpect(status().isOk());
    }
}