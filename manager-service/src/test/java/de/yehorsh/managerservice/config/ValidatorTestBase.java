package de.yehorsh.managerservice.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;

/**
 *
 * This class provides a reusable setup for tests that need to use
 * the javax.validation.Validator to validate beans or objects
 * against constraints like @NotNull, @NotBlank, @Pattern, @Email etc.
 *<p>
 * The Validator instance is initialized once, before all test methods,
 * and can be reused by any class that extends ValidatorTestBase.
 */
public class ValidatorTestBase {
    protected static Validator validator;

    // Initializes the Validator instance before any tests are run.
    @BeforeAll
    static void setup() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }
}
