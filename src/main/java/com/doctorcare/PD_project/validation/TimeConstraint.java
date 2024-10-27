package com.doctorcare.PD_project.validation;

import com.nimbusds.jose.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeValidation.class)
public @interface TimeConstraint {
    String message() default "TIME_INVALID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
