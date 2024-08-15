package com.bankapp.app.generator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class EmailDomainValidator implements ConstraintValidator<AllowedDomains, String> {

    private static final Set<String> allowedDomains = Set.of("gmail.com", "yahoo.com", "proton.me");

    @Override
    public void initialize(AllowedDomains constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null || !email.contains("@")) {
            return false;
        }

        String[] parts = email.split("@");
        if (parts.length != 2 || parts[0].isEmpty()) {
            return false;
        }

        String domain = parts[1];
        return allowedDomains.contains(domain);
    }
}
