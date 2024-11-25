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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(
        classes = AuthServiceApplication.class
        , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
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
        Role adminRole = roleRepository.findByName("MAIN_ADMIN")
                .orElseThrow(() -> new RuntimeException("MAIN ADMIN role not found"));

        userRepository.save(User.builder()
                .email("admin@example.com")
                .username("admin")
                .password(new BCryptPasswordEncoder().encode("password"))
                .roles(adminRole)
                .status(UserStatus.NEW)
                .build());
    }

    private String getAccessToken() throws Exception {
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto();
        userCredentialsDto.setEmail("admin@example.com");
        userCredentialsDto.setPassword("password");

        String loginJson = objectMapper.writeValueAsString(userCredentialsDto);

        String tokens = mockMvc.perform(MockMvcRequestBuilders.post("/auth/sing-in")
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
        createUserAndExpect(
                "BruceWayne",
                "fafe#1af@fAfa_",
                "bruce.wayne@example.com",
                "USER",
                201,
                "User was successfully created");

        assertThat(dbUtil.userExistsByEmail("bruce.wayne@example.com")).isTrue();

        // test
        User createUser = userRepository.findByEmail("bruce.wayne@example.com").get();

        // assert
        assertThat(createUser.getUserId()).isNotNull();
        assertThat(createUser.getUsername()).isEqualTo("BruceWayne");
        assertThat(passwordEncoder.matches("fafe#1af@fAfa_", createUser.getPassword())).isTrue();
        assertThat(createUser.getEmail()).isEqualTo("bruce.wayne@example.com");
        assertThat(createUser.getRoles().getName()).isEqualTo("USER");
        assertThat(createUser.getUpdateDate()).isNotNull();
    }

    @Test
    @WithUserDetails("admin@example.com")
    void test_findUser_success() throws Exception {
        // prepare
        User expectedUser = userService.createUser(
                new UserDto(
                        "BruceWayne",
                        passwordEncoder.encode("fafe#1af@fAfa_"),
                        "bruce@example.com",
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
            // Valid user details
            "'validUser', 'StrongP@ssw0rd!', 'valid.email@example.com', 'ADMIN', 201, 'User was successfully created'",

            // Invalid username: empty
            "'', 'StrongP@ssw0rd!', 'valid.email@example.com', 'ADMIN', 400, " +
                    "'Username can only contain letters, numbers, underscores, and hyphens; Username cannot be empty; Username must be between 4 and 20 characters'",

            // Invalid username: too short
            "'usr', 'StrongP@ssw0rd!', 'valid.email@example.com', 'ADMIN', 400, 'Username must be between 4 and 20 characters'",

            // Invalid username: invalid characters
            "'inv@lid', 'StrongP@ssw0rd!', 'valid.email@example.com', 'ADMIN', 400, 'Username can only contain letters, numbers, underscores, and hyphens'",

            // Invalid password: empty
            "'validUser', '', 'valid.email@example.com', 'ADMIN', 400," +
                    "'Password cannot be empty; Password must be between 8 and 64 characters; The password is incorrect. Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'",

            // Invalid password: does not meet the requirements (only lowercase letters)
            "'validUser', 'password', 'valid.email@example.com', 'ADMIN', 400, " +
                    "'The password is incorrect. Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'",

            // Invalid password: does not meet the requirements (no digit, no special character)
            "'validUser', 'Password', 'valid.email@example.com', 'ADMIN', 400, " +
                    "'The password is incorrect. Password is required to contain at least one uppercase and one lowercase, also one digit and one special character'",

            // Invalid email: empty
            "'validUser', 'StrongP@ssw0rd!', '', 'ADMIN', 400, 'Email should not be empty; Invalid email address'",

            // Invalid email: incorrect format
            "'validUser', 'StrongP@ssw0rd!', 'invalid-email', 'ADMIN', 400, " +
                    "'Email should be valid; Invalid email address'",

            // Duplicate email (if needed for testing unique constraints)
            "'validUser2', 'StrongP@ssw0rd!', 'valid.email@example.com', 'USER', 500, 'User with email valid.email@example.com already exists.'"
    })
    void test_createUser_invalidCases(String userName, String password, String email, String roleName, int expectedStatus, String expectedResponse) throws Exception {
        createUserAndExpect(userName, password, email, roleName, expectedStatus, expectedResponse);
    }

    private void createUserAndExpect(String userName, String password, String email, String roleName, int expectedStatus, String expectedResponse) throws Exception {
        UserDto newUserDto = new UserDto(userName, password, email, roleName);
        String userJson = objectMapper.writeValueAsString(newUserDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().string(expectedResponse));
    }
}