package ru.yandex.practicum.filmorate.unittests;

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
        var user = initUserBuilder()
                .email(null)
                .build();

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
        var user = initUserBuilder()
                .email("   ")
                .build();

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
        var user = initUserBuilder()
                .email("email")
                .build();
        var validMessages = validator.validate(user);

        Assertions.assertEquals(1, validMessages.size());

        Assertions.assertEquals(
                "must be a well-formed email address",
                validMessages
                        .stream().findFirst()
                        .map(ConstraintViolation::getMessage)
                        .orElse(""));

        user = initUserBuilder()
                .email("dfgd.email")
                .build();

        validMessages = validator.validate(user);

        Assertions.assertEquals(1, validMessages.size());

        user = initUserBuilder()
                .email("@email")
                .build();

        validMessages = validator.validate(user);

        Assertions.assertEquals(1, validMessages.size());

        Assertions.assertEquals(
                "must be a well-formed email address",
                validMessages
                        .stream().findFirst()
                        .map(ConstraintViolation::getMessage)
                        .orElse(""));

        user = initUserBuilder()
                .email("dfgd@.email")
                .build();

        validMessages = validator.validate(user);

        Assertions.assertEquals(1, validMessages.size());

        Assertions.assertEquals(
                "must be a well-formed email address",
                validMessages
                        .stream().findFirst()
                        .map(ConstraintViolation::getMessage)
                        .orElse(""));

        user = initUserBuilder()
                .email("dfgd@email.tr.")
                .build();

        validMessages = validator.validate(user);

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
        var user = initUserBuilder()
                .email("dfgd@email.ru")
                .build();

        var validMessages = validator.validate(user);

        Assertions.assertEquals(0, validMessages.size());
    }

    @Test
    void setNullLogin() {
        var user = initUserBuilder()
                .login(null)
                .build();

        var validMessages = validator.validate(user);

        validMessages = validMessages
                .stream()
                .filter(i -> !(i.getMessage().equals("must not be blank") && i.getPropertyPath().toString().equals("login")))
                .collect(Collectors.toSet());

        Assertions.assertEquals(0, validMessages.size());
    }

    @Test
    void setBlankLogin() {
        var user = initUserBuilder()
                .login("    ")
                .build();

        var validMessages = validator.validate(user);

        validMessages = validMessages
                .stream()
                .filter(i -> !(i.getMessage().equals("must not be blank") && i.getPropertyPath().toString().equals("login")))
                .collect(Collectors.toSet());

        Assertions.assertEquals(0, validMessages.size());
    }

    @Test
    void setNullName() {
        var user = initUserBuilder()
                .name(null)
                .build();

        Assertions.assertEquals("login", user.getName());
    }

    @Test
    void setBlankName() {
        var user = initUserBuilder()
                .name("   ")
                .build();

        Assertions.assertEquals("login", user.getName());
    }

    @Test
    void setFutureBirthDay() {
        var user = initUserBuilder()
                .birthday(LocalDate.now().plusDays(1))
                .build();

        var validMessages = validator.validate(user);

        validMessages = validMessages
                .stream()
                .filter(i -> !(i.getMessage().equals("Дата рождения не может быть в будущем") && i.getPropertyPath().toString().equals("birthday")))
                .collect(Collectors.toSet());

        Assertions.assertEquals(0, validMessages.size());
    }

    private User.UserBuilder initUserBuilder() {
        return User.builder()
                .id(1L)
                .email("dfgd@email.ru")
                .login("login")
                .birthday(LocalDate.now());
    }
}
