package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.stream.Collectors;


public class UserTest {
    private final Validator validator =
            Validation.buildDefaultValidatorFactory()
                    .getValidator();
    @Test
    void setNullEmail() {
        var user = new User(1L, null, "login", "", LocalDate.now().minusDays(1));

        var validMessages = validator.validate(user);

        Assertions.assertEquals(1, validMessages.size());

        Assertions.assertEquals(
                "must not be blank",
                validMessages
                        .stream().findFirst()
                        .map(ConstraintViolation::getMessage)
                        .orElse(""));
    }

    @Test
    void setBlankEmail() {
        var user = new User(1L, "   ", "login", "", LocalDate.now().minusDays(1));

        var validMessages = validator.validate(user);

        validMessages = validMessages
                .stream()
                .filter(i -> !i.getMessage().equals("must not be blank"))
                .filter(i -> !i.getMessage().equals("must be a well-formed email address"))
                .collect(Collectors.toSet());

        Assertions.assertEquals(0, validMessages.size());
    }

    @Test
    void setEmailNotFormat() {
        var validMessages = validator.validate(
                new User(1L, "email", "login", "", LocalDate.now().minusDays(1)));

        Assertions.assertEquals(1, validMessages.size());

        Assertions.assertEquals(
                "must be a well-formed email address",
                validMessages
                        .stream().findFirst()
                        .map(ConstraintViolation::getMessage)
                        .orElse(""));

        validMessages = validator.validate(
                        new User(1L, "dfgd.email", "login", "", LocalDate.now().minusDays(1)));

        Assertions.assertEquals(1, validMessages.size());

        validMessages = validator.validate(
                new User(1L, "@email", "login", "", LocalDate.now().minusDays(1)));

        Assertions.assertEquals(1, validMessages.size());

        Assertions.assertEquals(
                "must be a well-formed email address",
                validMessages
                        .stream().findFirst()
                        .map(ConstraintViolation::getMessage)
                        .orElse(""));

        validMessages = validator.validate(
                new User(1L, "dfgd.email", "login", "", LocalDate.now().minusDays(1)));

        Assertions.assertEquals(1, validMessages.size());

        Assertions.assertEquals(
                "must be a well-formed email address",
                validMessages
                        .stream().findFirst()
                        .map(ConstraintViolation::getMessage)
                        .orElse(""));

        validMessages = validator.validate(
                        new User(1L, "dfgd@email.", "login", "", LocalDate.now().minusDays(1)));

        Assertions.assertEquals(1, validMessages.size());

        Assertions.assertEquals(
                "must be a well-formed email address",
                validMessages
                        .stream().findFirst()
                        .map(ConstraintViolation::getMessage)
                        .orElse(""));

        validMessages = validator.validate(
                        new User(1L, "dfgd@.email", "login", "", LocalDate.now().minusDays(1)));

        Assertions.assertEquals(1, validMessages.size());

        Assertions.assertEquals(
                "must be a well-formed email address",
                validMessages
                        .stream().findFirst()
                        .map(ConstraintViolation::getMessage)
                        .orElse(""));

        validMessages = validator.validate(
                        new User(1L, "dfgd@email.tr.", "login", "", LocalDate.now().minusDays(1)));

        Assertions.assertEquals(1, validMessages.size());

        Assertions.assertEquals(
                "must be a well-formed email address",
                validMessages
                        .stream().findFirst()
                        .map(ConstraintViolation::getMessage)
                        .orElse(""));
    }

    @Test
    void setEmailFormat() {
        var validMessages = validator.validate(
                new User(1L, "dfgd@email.ru", "login", "", LocalDate.now().minusDays(1)));

        Assertions.assertEquals(0, validMessages.size());
    }

    @Test
    void setNullLogin() {
        try {
            var user = new User(1L, "sf@email.ru", null, "", LocalDate.now().minusDays(1));
        } catch (NullPointerException ex) {
            if (ex.getMessage().equals("login is marked non-null but is null"))
                return;
        }

        Assertions.fail();
    }

    @Test
    void setBlankLogin() {
        var user = new User(1L, "sf@email.ru", "   ", "", LocalDate.now().minusDays(1));

        var validMessages = validator.validate(user);

        validMessages = validMessages
                .stream()
                .filter(i -> !(i.getMessage().equals("must not be blank") && i.getPropertyPath().toString().equals("login")))
                .collect(Collectors.toSet());

        Assertions.assertEquals(0, validMessages.size());
    }

    @Test
    void setNullName() {
        var user = new User(1L, "sf@email.ru", "login", null, LocalDate.now().minusDays(1));

        Assertions.assertEquals("login", user.getName());
    }

    @Test
    void setBlankName() {
        var user = new User(1L, "sf@email.ru", "login", "  ", LocalDate.now().minusDays(1));

        Assertions.assertEquals("login", user.getName());
    }

    @Test
    void setFutureBirthDay() {
        var user = new User(1L, "sf@email.ru", "login", "", LocalDate.now().plusDays(1));

        var validMessages = validator.validate(user);

        validMessages = validMessages
                .stream()
                .filter(i -> !(i.getMessage().equals("Дата рождения не может быть в будущем") && i.getPropertyPath().toString().equals("birthday")))
                .collect(Collectors.toSet());

        Assertions.assertEquals(0, validMessages.size());
    }
}
