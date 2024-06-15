package ru.yandex.practicum.filmorate.tool;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class BirthDateUserValidator implements ConstraintValidator<BirthDateUserConstraint, LocalDate> {
    @Override
    public void initialize(BirthDateUserConstraint birthDate) {

    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
        return !value.isAfter(LocalDate.now());
    }
}
