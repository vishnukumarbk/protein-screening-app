package com.nuclera.protein.screening.app.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Sequence Validator
 */
@Documented
@Constraint(validatedBy = ValidScreeningSequencesValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidScreeningSequences {

    String message() default "must be 20 sequences with a common prefix and postfix of at least 5 amino acids "
                             + "and 2 to 9 amino acids between them";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
