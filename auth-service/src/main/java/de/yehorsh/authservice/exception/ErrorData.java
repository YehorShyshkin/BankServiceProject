package de.yehorsh.authservice.exception;

import org.springframework.http.HttpStatus;

public record ErrorData(HttpStatus httpStatus,
                        String message) {
}
