package com.bankapp.app.generator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailDomainValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface AllowedDomains {
    String message() default "Invalided email domain";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
