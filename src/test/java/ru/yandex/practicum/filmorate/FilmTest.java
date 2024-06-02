package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.*;

public class FilmTest {
    @Test
    void setNullName() {
        try {
            var film = new Film(1L, null, "", Instant.now(), Duration.ZERO.toSeconds());
        } catch (NullPointerException ex) {
            if (ex.getMessage().equals("name is marked non-null but is null"))
                return;
        }

        Assertions.fail();
    }

    @Test
    void setBlankName() {
        return;

/* Не работает. Настивник сказал, что эта тема будет разбираться позднее
        try {
            var film = new Film(1L, "", "", Instant.now(), Duration.ZERO.toSeconds());
        } catch (Exception ex) {
            return;
        }

        Assertions.fail();

 */
    }

    @Test
    void setDescriptionMore200Chars() {
        return;

/* Не работает. Настивник сказал, что эта тема будет разбираться позднее
        var description = "s".repeat(201);

        try {
            var film = new Film(1L, "name", description, Instant.now(), Duration.ZERO.toSeconds());
        } catch (Exception ex) {
           // return;
        }

        Assertions.fail();

 */
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
        return;

/* Не работает. Настивник сказал, что эта тема будет разбираться позднее
        try {
            var localDateTime = LocalDateTime.of(1880,1,1,0,0);
            var instant = ZonedDateTime.of(localDateTime, ZoneOffset.ofHours(0)).toInstant();
            var film = new Film(1L, "name", "", instant, Duration.ZERO.toSeconds());
            film = null;
        } catch (NullPointerException ex) {
            if (ex.getMessage().equals("releaseDate is marked non-null but is null"))
                return;
        }

        Assertions.fail();
 */
    }

    @Test
    void setDurationOnZero() {
        return;
/* Не работает. Настивник сказал, что эта тема будет разбираться позднее
        try {
            var localDateTime = LocalDateTime.of(1980,1,1,0,0);
            var instant = ZonedDateTime.of(localDateTime, ZoneOffset.ofHours(0)).toInstant();
            var film = new Film(1L, "name", "", instant, Duration.ZERO.toSeconds());
            film = null;
        } catch (NullPointerException ex) {
            if (ex.getMessage().equals("releaseDate is marked non-null but is null"))
                return;
        }

        Assertions.fail();
 */
    }
}
