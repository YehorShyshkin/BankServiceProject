package de.yehorsh.customerservice.exception;

import org.springframework.http.HttpStatus;

public record ErrorData(HttpStatus httpStatus,
                        String message) {
}
