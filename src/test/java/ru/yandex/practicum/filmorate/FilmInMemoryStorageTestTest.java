package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.IdIsAlreadyInUseException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SortOrder;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public class FilmInMemoryStorageTestTest {
    @Test
    void deleteFilm() {
        FilmStorage filmStorage = new InMemoryFilmStorage();

        filmStorage.createFilm(initFilmBuilder(1)
                .name("film1")
                .build());

        filmStorage.createFilm(initFilmBuilder(2)
                .name("film2")
                .build());

        filmStorage.createFilm(initFilmBuilder(3)
                .name("film3")
                .build());

        filmStorage.deleteFilm(2);

        Assertions.assertEquals(2, filmStorage.getFilms(SortOrder.UNKNOWN, Optional.empty(), Optional.empty()).size());
    }

    @Test
    void updateFilm() {
        FilmStorage filmStorage = new InMemoryFilmStorage();

        filmStorage.createFilm(initFilmBuilder(1)
                .name("film1")
                .build());

        filmStorage.updateFilm(initFilmBuilder(1)
                .name("film2")
                .build());

        Assertions.assertEquals("film2", filmStorage.getFilm(1).getName());
    }

    @Test
    void createFilm() {
        FilmStorage filmStorage = new InMemoryFilmStorage();

        filmStorage.createFilm(initFilmBuilder(1)
                .name("film1")
                .build());

        Assertions.assertEquals("film1", filmStorage.getFilm(1).getName());
    }

    @Test
    void createNotUniqueFilm() {
        FilmStorage filmStorage = new InMemoryFilmStorage();

        filmStorage.createFilm(initFilmBuilder(1)
                .name("film1")
                .build());

        Assertions.assertThrows(
                IdIsAlreadyInUseException.class,
                () -> filmStorage.createFilm(initFilmBuilder(1)
                .name("film2")
                .build()));

        Assertions.assertEquals(1, filmStorage.getFilms(SortOrder.UNKNOWN, Optional.empty(), Optional.empty()).size());
    }

    @Test
    void createThreeFilms() {
        FilmStorage filmStorage = new InMemoryFilmStorage();

        filmStorage.createFilm(initFilmBuilder(1)
                .name("film1")
                .build());

        filmStorage.createFilm(initFilmBuilder(2)
                .name("film2")
                .build());

        filmStorage.createFilm(initFilmBuilder(3)
                .name("film3")
                .build());

        Assertions.assertEquals(3, filmStorage.getFilms(SortOrder.UNKNOWN, Optional.empty(), Optional.empty()).size());
    }

    @Test
    void containsKeyIsTrue() {
        FilmStorage filmStorage = new InMemoryFilmStorage();

        filmStorage.createFilm(initFilmBuilder(1)
                .name("film1")
                .build());

        filmStorage.createFilm(initFilmBuilder(2)
                .name("film2")
                .build());

        filmStorage.createFilm(initFilmBuilder(3)
                .name("film3")
                .build());

        Assertions.assertTrue(filmStorage.containsKey(2));
    }

    @Test
    void containsKeyIsFalse() {
        FilmStorage filmStorage = new InMemoryFilmStorage();

        filmStorage.createFilm(initFilmBuilder(1)
                .name("film1")
                .build());

        filmStorage.createFilm(initFilmBuilder(2)
                .name("film2")
                .build());

        filmStorage.createFilm(initFilmBuilder(3)
                .name("film3")
                .build());

        Assertions.assertFalse(filmStorage.containsKey(20));
    }

    @Test
    void getFilm() {
        FilmStorage filmStorage = new InMemoryFilmStorage();

        filmStorage.createFilm(initFilmBuilder(1)
                .name("film1")
                .build());

        filmStorage.createFilm(initFilmBuilder(2)
                .name("film2")
                .build());

        filmStorage.createFilm(initFilmBuilder(3)
                .name("film3")
                .build());

        Assertions.assertEquals("film3", filmStorage.getFilm(3).getName());
    }

    @Test
    void getSortedFilm() {
        FilmStorage filmStorage = new InMemoryFilmStorage();

        filmStorage.createFilm(initFilmBuilder(1)
                .name("film1")
                .likes(Set.of(5L, 6L))
                .build());

        filmStorage.createFilm(initFilmBuilder(2)
                .name("film2")
                .likes(Set.of(5L))
                .build());

        filmStorage.createFilm(initFilmBuilder(3)
                .name("film3")
                .likes(Set.of(5L, 6L, 8L))
                .build());

        Assertions.assertEquals(2L, filmStorage.getFilms(SortOrder.ASCENDING, Optional.empty(), Optional.empty()).getFirst().getId());
    }

    @Test
    void getSortedDescFilm() {
        FilmStorage filmStorage = new InMemoryFilmStorage();

        filmStorage.createFilm(initFilmBuilder(1)
                .name("film1")
                .likes(Set.of(5L, 6L))
                .build());

        filmStorage.createFilm(initFilmBuilder(2)
                .name("film2")
                .likes(Set.of(5L))
                .build());

        filmStorage.createFilm(initFilmBuilder(3)
                .name("film3")
                .likes(Set.of(5L, 6L, 8L))
                .build());

        Assertions.assertEquals(3L, filmStorage.getFilms(SortOrder.DESCENDING, Optional.empty(), Optional.empty()).getFirst().getId());
    }

    @Test
    void getSkipFilms() {
        FilmStorage filmStorage = new InMemoryFilmStorage();

        filmStorage.createFilm(initFilmBuilder(1)
                .name("film1")
                .likes(Set.of(5L, 6L))
                .build());

        filmStorage.createFilm(initFilmBuilder(2)
                .name("film2")
                .likes(Set.of(5L))
                .build());

        filmStorage.createFilm(initFilmBuilder(3)
                .name("film3")
                .likes(Set.of(5L, 6L, 8L))
                .build());

        Assertions.assertEquals(2, filmStorage.getFilms(SortOrder.UNKNOWN, Optional.empty(), Optional.of(1)).size());
    }

    @Test
    void getFirst2Films() {
        FilmStorage filmStorage = new InMemoryFilmStorage();

        filmStorage.createFilm(initFilmBuilder(1)
                .name("film1")
                .likes(Set.of(5L, 6L))
                .build());

        filmStorage.createFilm(initFilmBuilder(2)
                .name("film2")
                .likes(Set.of(5L))
                .build());

        filmStorage.createFilm(initFilmBuilder(3)
                .name("film3")
                .likes(Set.of(5L, 6L, 8L))
                .build());

        Assertions.assertEquals(2, filmStorage.getFilms(SortOrder.UNKNOWN, Optional.of(2), Optional.empty()).size());
    }

    @Test
    void get2stFilm() {
        FilmStorage filmStorage = new InMemoryFilmStorage();

        filmStorage.createFilm(initFilmBuilder(1)
                .name("film1")
                .likes(Set.of(5L, 6L))
                .build());

        filmStorage.createFilm(initFilmBuilder(2)
                .name("film2")
                .likes(Set.of(5L))
                .build());

        filmStorage.createFilm(initFilmBuilder(3)
                .name("film3")
                .likes(Set.of(5L, 6L, 8L))
                .build());

        filmStorage.createFilm(initFilmBuilder(4)
                .name("film3")
                .likes(Set.of(5L, 6L, 8L, 10L))
                .build());

        var films = filmStorage.getFilms(SortOrder.DESCENDING, Optional.of(2), Optional.of(1));

        Assertions.assertEquals(2, films.size());

        Assertions.assertEquals(3L, films.getFirst().getId());
    }

    private Film.FilmBuilder initFilmBuilder(int id) {
        return Film.builder()
                .id((long) id)
                .name("name")
                .description("")
                .releaseDate(LocalDate.now())
                .duration(1);
    }

}
