package ru.yandex.practicum.filmorate.tool;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;

public class BirthDateUserValidator implements ConstraintValidator<BirthDateUserConstraint, Instant> {
    @Override
    public void initialize(BirthDateUserConstraint BirthDate) {

    }

    @Override
    public boolean isValid(Instant value, ConstraintValidatorContext constraintValidatorContext) {
        return false;
        //return !value.isAfter(Instant.now());
    }
}
