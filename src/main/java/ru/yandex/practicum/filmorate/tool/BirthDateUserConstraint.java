package ru.yandex.practicum.filmorate.tool;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BirthDateUserValidator.class)
public @interface BirthDateUserConstraint {
    String message() default "Дата рождения не может быть в будущем";
}
