package com.bankapp.app.request;

import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

public record ErrorData(HttpStatus status,
                        OffsetDateTime timestamp,
                        String message,
                        String details) {
}
