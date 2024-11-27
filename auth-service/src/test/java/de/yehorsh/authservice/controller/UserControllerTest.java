package de.yehorsh.authservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import de.yehorsh.authservice.AuthServiceApplication;
import de.yehorsh.authservice.config.ContainersEnvironment;
import de.yehorsh.authservice.config.TestSecurityConfig;
import de.yehorsh.authservice.dto.JwtAuthenticationDto;
import de.yehorsh.authservice.dto.UserCredentialsDto;
import de.yehorsh.authservice.dto.UserDto;
import de.yehorsh.authservice.model.entity.Role;
import de.yehorsh.authservice.model.entity.User;
import de.yehorsh.authservice.model.enums.RoleName;
import de.yehorsh.authservice.model.enums.UserStatus;
import de.yehorsh.authservice.repository.RoleRepository;
import de.yehorsh.authservice.repository.UserRepository;
import de.yehorsh.authservice.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(classes = AuthServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {ContainersEnvironment.Initializer.class})
@Import(TestSecurityConfig.class)
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

    @BeforeAll
    static void setUpDatabase(@Autowired UserRepository userRepository, @Autowired RoleRepository roleRepository) {
        // Ensure roles exist
        for (RoleName roleName : RoleName.values()) {
            if (!roleRepository.existsByName(roleName.name())) {
                Role role = new Role();
                role.setId(UUID.randomUUID());
                role.setName(String.valueOf(roleName));
                roleRepository.save(role);
            }
        }

        // Add admin user
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

        userRepository.save(User.builder()
                .email("admin@example.com")
                .password(new BCryptPasswordEncoder().encode("StrongP@ssw0rd!"))
                .roles(adminRole)
                .status(UserStatus.NEW)
                .build());
    }

    private String getAccessToken() throws Exception {
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto();
        userCredentialsDto.setEmail("admin@example.com");
        userCredentialsDto.setPassword("StrongP@ssw0rd!");

        String loginJson = objectMapper.writeValueAsString(userCredentialsDto);

        String tokens = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JwtAuthenticationDto jwtAuthenticationDto = objectMapper.readValue(tokens, JwtAuthenticationDto.class);

        return jwtAuthenticationDto.getToken();
    }

    @Test
    void test_createUser_success() throws Exception {
        // prepare
        MvcResult result = createUserAndExpect(
                "bruce.wayne@example.com",
                "fafe#1af@fAfa_",
                "MANAGER",
                201
        );

        String userId = result.getResponse().getContentAsString();

        assertThat(dbUtil.userExistsByEmail("bruce.wayne@example.com")).isTrue();

        // test
        User createUser = userRepository.findByEmail("bruce.wayne@example.com").get();

        // assert
        assertThat(createUser.getUserId().toString()).isEqualTo(userId);
        assertThat(createUser.getUserId()).isNotNull();
        assertThat(passwordEncoder.matches("fafe#1af@fAfa_", createUser.getPassword())).isTrue();
        assertThat(createUser.getEmail()).isEqualTo("bruce.wayne@example.com");
        assertThat(createUser.getRoles().getName()).isEqualTo("MANAGER");
        assertThat(createUser.getUpdateDate()).isNotNull();
    }

    @Test
    @WithUserDetails("admin@example.com")
    void test_findUser_success() throws Exception {
        // prepare
        User expectedUser = userService.createUser(
                new UserDto(
                        "bruce@example.com",
                        passwordEncoder.encode("fafe#1af@fAfa_"),
                        "MANAGER"
                ), RoleName.MANAGER);

        String token = getAccessToken();

        // test
        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + expectedUser.getUserId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("bruce@example.com"));

        // assert
        assertThat(dbUtil.userExistsByEmail("bruce@example.com")).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            // Successful user creation
            "'valid.email@example.com','StrongP@ssw0rd!', 'ADMIN', 201, ''",

            // Error: empty password
            "'valid.email@example.com','','ADMIN', 400, " +
                    "'Password cannot be empty; Password must be between 8 and 64 characters; The password is incorrect. Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'",

            // Error: password not meeting requirements
            "'valid.email@example.com','password', 'ADMIN', 400, " +
                    "'The password is incorrect. Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'",

            // Error: invalid email format
            "'invalid-email','StrongP@ssw0rd!','ADMIN', 400,'Email should be valid; Invalid email address'",

            // Error: duplicate email
            "'valid.email@example.com','StrongP@ssw0rd!', 'MANAGER', 500, 'User with email valid.email@example.com already exists.'"
    })
    void test_createUser_invalidCases(String email, String password,  String roleName, int expectedStatus, String expectedResponse) throws Exception {
        MvcResult result = createUserAndExpect(email, password, roleName, expectedStatus);
        String actualResponse = result.getResponse().getContentAsString();
        if (expectedStatus == 201) {
            assertThat(actualResponse).matches("^[a-f0-9\\-]{36}$");
        } else {
            assertThat(actualResponse).isEqualTo(expectedResponse);
        }
    }

    private MvcResult createUserAndExpect(String email, String password, String roleName, int expectedStatus) throws Exception {
        UserDto newUserDto = new UserDto(email, password, roleName);
        String userJson = objectMapper.writeValueAsString(newUserDto);

        return mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(expectedStatus))
                .andReturn();
    }
}