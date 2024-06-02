package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Storage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.Instant;

public class StorageTest {
    @Test
    void addFilm() {
        var storage = new Storage();

        storage.updateFilm(new Film(1L, "", "", Instant.now(), Duration.ZERO.toSeconds()));
        storage.updateFilm(new Film(2L, "", "", Instant.now(), Duration.ZERO.toSeconds()));

        Assertions.assertEquals(2, storage.getFilms().size());
    }

    @Test
    void addDoubleFilm() {
        var storage = new Storage();

        storage.updateFilm(new Film(1L, "", "", Instant.now(), Duration.ZERO.toSeconds()));
        storage.updateFilm(new Film(1L, "", "", Instant.now(), Duration.ZERO.toSeconds()));

        Assertions.assertEquals(1, storage.getFilms().size());
    }

    @Test
    void updateFilm() {
        var storage = new Storage();

        storage.updateFilm(new Film(1L, "", "", Instant.now(), Duration.ZERO.toSeconds()));
        storage.updateFilm(new Film(2L, "", "", Instant.now(), Duration.ZERO.toSeconds()));

        storage.updateFilm(new Film(2L, "test", "", Instant.now(), Duration.ZERO.toSeconds()));

        Assertions.assertEquals("test", storage.getFilm(2L).getName());
    }

    @Test
    void addUser() {
        var storage = new Storage();

        storage.updateUser(new User(1L, "", "", "name", Instant.now()));
        storage.updateUser(new User(2L, "", "", "name", Instant.now()));

        Assertions.assertEquals(2, storage.getUsers().size());
    }

    @Test
    void addDoubleUser() {
        var storage = new Storage();

        storage.updateUser(new User(1L, "", "", "name", Instant.now()));
        storage.updateUser(new User(1L, "", "", "name", Instant.now()));

        Assertions.assertEquals(1, storage.getUsers().size());
    }

    @Test
    void updateUser() {
        var storage = new Storage();

        storage.updateUser(new User(1L, "1L", "","name", Instant.now()));
        storage.updateUser(new User(2L, "", "", "name", Instant.now()));

        storage.updateUser(new User(2L, "test", "", "test", Instant.now()));

        Assertions.assertEquals("test", storage.getUser(2L).getName());
    }
}
