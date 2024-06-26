package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Storage;

import java.time.Duration;
import java.time.LocalDate;

public class FilmControllerTests {

    @Test
    void getFilm() throws Exception {
        var storage = new Storage();
        var controller = new FilmController(storage);

        var expectedFilm = new Film(2L, "2L", "", LocalDate.now(), Duration.ZERO.toSeconds());

        storage.updateFilm(new Film(1L, "1L", "", LocalDate.now(), Duration.ZERO.toSeconds()));
        storage.updateFilm(expectedFilm);
        storage.updateFilm(new Film(3L, "3L", "", LocalDate.now(), Duration.ZERO.toSeconds()));

        var actualFilm = controller.getFilm(2L);

        Assertions.assertEquals(expectedFilm, actualFilm);
    }

    @Test
    void getFilms() {
        var storage = new Storage();
        var controller = new FilmController(storage);

        storage.updateFilm(new Film(1L, "1L", "", LocalDate.now(), Duration.ZERO.toSeconds()));
        storage.updateFilm(new Film(2L, "2L", "", LocalDate.now(), Duration.ZERO.toSeconds()));
        storage.updateFilm(new Film(3L, "3L", "", LocalDate.now(), Duration.ZERO.toSeconds()));

        Assertions.assertEquals(3, controller.getFilms().size());
    }

    @Test
    void addFilm() {
        var storage = new Storage();
        var controller = new FilmController(storage);

        storage.updateFilm(new Film(1L, "1L", "", LocalDate.now(), Duration.ZERO.toSeconds()));
        storage.updateFilm(new Film(2L, "2L", "", LocalDate.now(), Duration.ZERO.toSeconds()));
        storage.updateFilm(new Film(3L, "3L", "", LocalDate.now(), Duration.ZERO.toSeconds()));

        controller.updateFilm(new Film(4L, "4L", "", LocalDate.now(), Duration.ZERO.toSeconds()));

        Assertions.assertEquals(4, controller.getFilms().size());
    }

    @Test
    void updateFilm() throws Exception {
        var storage = new Storage();
        var controller = new FilmController(storage);

        storage.updateFilm(new Film(1L, "1L", "", LocalDate.now(), Duration.ZERO.toSeconds()));
        storage.updateFilm(new Film(2L, "2L", "", LocalDate.now(), Duration.ZERO.toSeconds()));
        storage.updateFilm(new Film(3L, "3L", "", LocalDate.now(), Duration.ZERO.toSeconds()));

        controller.updateFilm(new Film(2L, "10L", "", LocalDate.now(), Duration.ZERO.toSeconds()));

        Assertions.assertEquals("10L", controller.getFilm(2L).getName());
    }
}
