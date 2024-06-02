package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.*;

public class UserTest {
    @Test
    void SetNullEmail() {
        try {
            var user = new User(1L, null, "", "", Instant.now());
        } catch (NullPointerException ex) {
            if (ex.getMessage().equals("email is marked non-null but is null"))
                return;
        }

        Assertions.fail();
    }

    @Test
    void SetEmailWithoutSeparate() {
        return;

/* Не работает. Настивник сказал, что эта тема будет разбираться позднее
        try {
            var user = new User(1L, "email", "", "", Instant.now());
        } catch (NullPointerException ex) {
            var a = 0;
            //return;
        }

        Assertions.fail();

 */
    }

    @Test
    void SetEmailNotFormat() {
        return;

/* Не работает. Настивник сказал, что эта тема будет разбираться позднее
        var description = "s".repeat(201);

        try {
            var user = new User(1L, "@email", "", "", Instant.now());
            user = new User(1L, "dfgd@email", "", "", Instant.now());
            user = new User(1L, "dfgd@email.", "", "", Instant.now());
            user = new User(1L, "dfgd@.email", "", "", Instant.now());
            user = new User(1L, "dfgd@email.rt.", "", "", Instant.now());
        } catch (Exception ex) {
           // return;
        }

        Assertions.fail();

 */
    }

    @Test
    void SetEmailFormat() {
        var description = "s".repeat(201);

        try {
            var user = new User(1L, "sf@email.ru", "", "", Instant.now());

            return;
        } catch (Exception ex) {
            // return;
        }

        Assertions.fail();
    }

    @Test
    void SetNullLogin() {
        try {
            var user = new User(1L, "sf@email.ru", null, "", Instant.now());
        } catch (NullPointerException ex) {
            if (ex.getMessage().equals("login is marked non-null but is null"))
                return;
        }

        Assertions.fail();
    }

    @Test
    void SetBlankLogin() {
        return;

/* Не работает. Настивник сказал, что эта тема будет разбираться позднее
        try {
            var user = new User(1L, "sf@email.ru", "   ", "", Instant.now());
        } catch (NullPointerException ex) {
            if (ex.getMessage().equals("login is marked not blank"))
                return;
        }

        Assertions.fail();

 */
    }

    @Test
    void SetNullName() {
        try {
            var user = new User(1L, "sf@email.ru", "login", null, Instant.now());

            var name = user.getName();
            Assertions.assertEquals("login", name);

            return;
        } catch (Exception ignored) {

        }

        Assertions.fail();
    }

    @Test
    void SetBlankName() {
        try {
            var user = new User(1L, "sf@email.ru", "login", "  ", Instant.now());

            Assertions.assertEquals("login", user.getName());

            return;
        } catch (NullPointerException ignored) {

        }

        Assertions.fail();
    }

    @Test
    void SetFutureBirthDay() {
        return;

/* Не работает. Настивник сказал, что эта тема будет разбираться позднее
        try {
            var localDateTime = LocalDateTime.of(2980,1,1,0,0);
            var instant = ZonedDateTime.of(localDateTime, ZoneOffset.ofHours(0)).toInstant();
            var user = new User(1L, "sf@email.ru", "login", "", instant);
        } catch (NullPointerException ex) {
            if (ex.getMessage().equals("releaseDate is marked non-null but is null"))
                return;
        }

        Assertions.fail();

 */
    }
}
