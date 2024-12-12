package de.yehorsh.authservice.controller;

import de.yehorsh.authservice.dto.UserCredentialsDto;
import de.yehorsh.authservice.dto.JwtAuthenticationDto;
import de.yehorsh.authservice.dto.RefreshTokenDto;
import de.yehorsh.authservice.exception.AuthenticationException;
import de.yehorsh.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationDto> singIn(@RequestBody UserCredentialsDto userCredentialsDto) {
        try {
            JwtAuthenticationDto jwtAuthenticationDto = userService.singIn(userCredentialsDto);
            return ResponseEntity.status(HttpStatus.OK).body(jwtAuthenticationDto);

        } catch (AuthenticationException e) {
            log.warn("Authentication failed for user: {}", userCredentialsDto.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationDto> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        try {
            JwtAuthenticationDto jwtAuthenticationDto = userService.refreshToken(refreshTokenDto);
            return ResponseEntity.status(HttpStatus.OK).body(jwtAuthenticationDto);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
