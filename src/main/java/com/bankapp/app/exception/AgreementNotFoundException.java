package com.bankapp.app.exception;

public class AgreementNotFoundException extends RuntimeException {
    public AgreementNotFoundException(String message) {
        super(message);
    }
}
