package com.doctorcare.PD_project.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.util.Objects;

public class ScheduleValidation implements ConstraintValidator<ScheduleConstraint, LocalDateTime> {

    @Override
    public void initialize(ScheduleConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if(Objects.isNull(value))
            return true;
        return !value.isBefore(LocalDateTime.now());
    }
}
