package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.*;

public class FilmTest {
    private final Validator validator =
            Validation.buildDefaultValidatorFactory()
                .getValidator();

    @Test
    void setNullName() {
        var film = new Film(1L, null, "", LocalDate.now(), 1L);

        var validMessages = validator.validate(film);

        Assertions.assertEquals(1, validMessages.size());

        Assertions.assertEquals(
                "must not be blank",
                validMessages
                        .stream().findFirst()
                        .map(ConstraintViolation::getMessage)
                        .orElse(""));
    }

    @Test
    void setBlankName() {
        var film = new Film(1L, null, "", LocalDate.now(), 1L);

        var validMessages = validator.validate(film);

        Assertions.assertEquals(1, validMessages.size());

        Assertions.assertEquals(
                "must not be blank",
                validMessages
                        .stream().findFirst()
                        .map(ConstraintViolation::getMessage)
                        .orElse(""));
    }

    @Test
    void setDescriptionMore200Chars() {
        var description = "s".repeat(201);

        var film = new Film(1L, "name", description, LocalDate.now(), 1L);

        var validMessages = validator.validate(film);

        Assertions.assertEquals(1, validMessages.size());

        Assertions.assertEquals(
                "size must be between 0 and 200",
                validMessages
                        .stream().findFirst()
                        .map(ConstraintViolation::getMessage)
                        .orElse(""));
    }

    @Test
    void setNullReleaseDate() {
        try {
            var film = new Film(1L, "name", "", null, Duration.ZERO.toSeconds());
        } catch (NullPointerException ex) {
            if (ex.getMessage().equals("releaseDate is marked non-null but is null"))
                return;
        }

        Assertions.fail();
    }

    @Test
    void setReleaseDateEarlyBurnCinema() {
        var film = new Film(1L, "name", "", LocalDate.of(1880,1,1), 1L);

        var validMessages = validator.validate(film);

        Assertions.assertEquals(1, validMessages.size());

        Assertions.assertEquals(
                "Дата фильма не ранее 28 декабря 1895 года",
                validMessages
                        .stream().findFirst()
                        .map(ConstraintViolation::getMessage)
                        .orElse(""));
    }

    @Test
    void setDurationOnZero() {
        var film = new Film(1L, "name", "", LocalDate.now(), 0L);

        var validMessages = validator.validate(film);

        Assertions.assertEquals(1, validMessages.size());

        Assertions.assertEquals(
                "must be greater than or equal to 1",
                validMessages
                        .stream().findFirst()
                        .map(ConstraintViolation::getMessage)
                        .orElse(""));
    }
}
