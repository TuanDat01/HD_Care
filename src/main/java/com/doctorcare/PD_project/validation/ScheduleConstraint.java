package com.doctorcare.PD_project.validation;

import com.nimbusds.jose.Payload;
import jakarta.validation.Constraint;


import java.lang.annotation.Retention;

import java.lang.annotation.Target;


import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ScheduleValidation.class)
public @interface ScheduleConstraint {
    String message() default "{jakarta.validation.constraints.Size.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
