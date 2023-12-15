package com.bankapp.app.generator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailDomainValidator.class)
/**
 * ----- Russian ------
 * <p>
 * Эта аннотация используется только на полях
 * <p>
 * ----- English -------
 * This annotation is used only in the margins
 */
@Target({ElementType.FIELD})

/**
 * ----- Russian ------
 * <p>
 * Аннотация говорит о том, где эта аннотация может быть использована,
 * в данном случае в режиме выполнения.
 * <p>
 *  ----- English -------
 * An annotation tells where this annotation can be used,
 * in this case in runtime mode.
 */
@Retention(RetentionPolicy.RUNTIME)

public @interface AllowedDomains {
    String message() default "Invalided email domain";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
