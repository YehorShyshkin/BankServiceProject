package com.bankapp.app.exception;

public class FailedToSaveManagerException extends RuntimeException {
    public FailedToSaveManagerException(String message, Exception e) {
        super(message);
    }
}
