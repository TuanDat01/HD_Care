package com.doctorcare.PD_project.validation;

import com.doctorcare.PD_project.entity.Schedule;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class TimeValidation implements ConstraintValidator<TimeConstraint, Schedule> {
    @Override
    public void initialize(TimeConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Schedule value, ConstraintValidatorContext context) {
        if(Objects.isNull(value))
            return true;
        return value.getEnd().isAfter(value.getStart());
    }

}
