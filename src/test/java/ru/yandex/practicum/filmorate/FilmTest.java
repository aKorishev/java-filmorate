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
        var film = initFilmBuilder()
                .name(null)
                .build();

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
        var film = initFilmBuilder()
                .name("   ")
                .build();

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

        var film = initFilmBuilder()
                .description(description)
                .build();

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
        var exception = Assertions.assertThrows(
                NullPointerException.class,
                () -> initFilmBuilder()
                        .releaseDate(null)
                        .build());

        Assertions.assertEquals("releaseDate is marked non-null but is null", exception.getMessage());
    }

    @Test
    void setReleaseDateEarlyBurnCinema() {
        var film = initFilmBuilder()
                .releaseDate(LocalDate.of(1880,1,1))
                .build();

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
        var film = initFilmBuilder()
                .duration(0)
                .build();

        var validMessages = validator.validate(film);

        Assertions.assertEquals(1, validMessages.size());

        Assertions.assertEquals(
                "must be greater than or equal to 1",
                validMessages
                        .stream().findFirst()
                        .map(ConstraintViolation::getMessage)
                        .orElse(""));
    }


    private Film.FilmBuilder initFilmBuilder() {
        return Film.builder()
                .id(1L)
                .name("name")
                .description("")
                .releaseDate(LocalDate.now())
                .duration(1);
    }

}
