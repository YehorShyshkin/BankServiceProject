package de.yehorsh.managerservice.exception;

import org.springframework.http.HttpStatus;

public record ErrorData(HttpStatus httpStatus,
                        String message) {
}

