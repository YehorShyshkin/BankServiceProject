package de.yehorsh.authservice.security.jwt;

import de.yehorsh.authservice.dto.JwtAuthenticationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private final String EMAIL = "test@test.de";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService.setJwtSecret("d91fb2a36c4ad89da9f322bcfcd7b357294eb77e8c44d5b36695f449ddacc76f");
        jwtService.setTokenTtlMinutes(1);
    }

    @Test
    void test_generateAuthToken_success() {
        JwtAuthenticationDto jwtAuthenticationDto = jwtService.generateAuthToken(EMAIL);
        assertThat(jwtAuthenticationDto.getToken()).isNotNull();
        assertThat(EMAIL).isEqualTo(jwtService.getEmailFromToken(jwtAuthenticationDto.getToken()));
        assertThat(jwtService.validateJwtToken(jwtAuthenticationDto.getToken())).isTrue();
    }

    @Test
    void test_refreshBaseToken_success() throws InterruptedException {
        JwtAuthenticationDto jwtAuthenticationDto = jwtService.generateAuthToken(EMAIL);

        Thread.sleep(1000);

        JwtAuthenticationDto jwtAuthenticationDtoNew =
                jwtService.refreshBaseToken(EMAIL, jwtAuthenticationDto.getRefreshToken());
        assertThat(jwtAuthenticationDtoNew).isNotNull();
        assertThat(jwtAuthenticationDto.getRefreshToken()).isEqualTo(jwtAuthenticationDtoNew.getRefreshToken());
        assertThat(jwtAuthenticationDtoNew.getToken()).isNotEqualTo(jwtAuthenticationDto.getToken());
    }

    @Test
    void test_getEmailFromToken_success() {
        JwtAuthenticationDto jwtAuthenticationDto = jwtService.generateAuthToken(EMAIL);
        assertThat(EMAIL).isEqualTo(jwtService.getEmailFromToken(jwtAuthenticationDto.getToken()));
    }

    @Test
    void test_validateJwtToken_success() {
        JwtAuthenticationDto jwtAuthenticationDto = jwtService.generateAuthToken(EMAIL);
        assertThat(jwtService.validateJwtToken(jwtAuthenticationDto.getToken())).isTrue();
    }

    @Test
    void test_JwtToken_neValid() {
        assertThat(jwtService.validateJwtToken("eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuZGUiLCJleHAiOjE3MzI2MTU2MDZ9" +
                ".QL3DhSvU8FXAWWVnDQTJX2XwjGveEd4vgyWB_DC0n9ZUphHL59C7s1qiuLhZFDUa")).isFalse();
    }

    @Test
    void test_UnsupportedJwtException() {
        String unsupportedToken = "eyJhbGciOiJIUzUxMiJ9" +
                ".eyJzdWIiOiJ1bnN1cHBvcnRlZEBleGFtcGxlLmNvbSIsImV4cCI6MTcwMDAwMDAwMH0.signature";
        assertThat(jwtService.validateJwtToken(unsupportedToken)).isFalse();
    }

    @Test
    void test_MalformedJwtException() {
        String malformedToken = "eyJhbGciOiJIUzI1NiJ9.malformed_payload.signature";
        assertThat(jwtService.validateJwtToken(malformedToken)).isFalse();
    }

    @Test
    void test_SecurityException() {
        String invalidSignatureToken = "eyJhbGciOiJIUzI1NiJ9" +
                ".eyJzdWIiOiJzZWN1cml0eUBleGFtcGxlLmNvbSIsImV4cCI6MTcwMDAwMDAwMH0.wrong_signature";
        assertThat(jwtService.validateJwtToken(invalidSignatureToken)).isFalse();
    }

    @Test
    void test_GenericException() {
        String invalidToken = "invalid-token-string";
        assertThat(jwtService.validateJwtToken(invalidToken)).isFalse();
    }
}