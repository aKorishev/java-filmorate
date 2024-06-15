package ru.yandex.practicum.filmorate.tool;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ReleaseDateFilmValidator implements ConstraintValidator<ReleaseDateFilmConstraint, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
        var minDate = LocalDate.of(1895,12,28);
        return !value.isBefore(minDate);
    }
}

