package ru.yandex.practicum.filmorate.tool;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;
import java.util.Date;

public class ReleaseDateFilmValidator implements ConstraintValidator<ReleaseDateFilmConstraint, Instant> {

    @Override
    public boolean isValid(Instant value, ConstraintValidatorContext constraintValidatorContext) {
        var minDate =  new Date(1985,12,28).toInstant();
        return !minDate.isBefore(value);
    }
}

