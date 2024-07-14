package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.IdIsAlreadyInUseException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SortOrder;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public class FilmServiceTest {
    @Test
    void getFilm() {
        var filmService = new FilmService(
                new InMemoryFilmStorage(),
                new InMemoryUserStorage());

        filmService.postFilm(initFilmBuilder(1).build());

        Assertions.assertNotNull(filmService.getFilm(1L));
    }

    @Test
    void getThrowOnGetFilm() {
        var filmService = new FilmService(
                new InMemoryFilmStorage(),
                new InMemoryUserStorage());

        filmService.postFilm(
                initFilmBuilder(1)
                        .build());

        Assertions.assertThrows(NotFoundException.class, () -> filmService.getFilm(3L));
    }

    @Test
    void get2stFilm() {
        var filmService = new FilmService(
                new InMemoryFilmStorage(),
                new InMemoryUserStorage());

        filmService.postFilm(
                initFilmBuilder(1)
                        .name("film1")
                        .likes(Set.of(1L))
                        .build());

        filmService.postFilm(
                initFilmBuilder(2)
                        .name("film2")
                        .likes(Set.of())
                        .build());

        filmService.postFilm(
                initFilmBuilder(3)
                        .name("film3")
                        .likes(Set.of(1L, 4L, 5L))
                        .build());

        filmService.postFilm(
                initFilmBuilder(4)
                        .name("film4")
                        .likes(Set.of(1L, 4L))
                        .build());

        var films = filmService.getFilms(SortOrder.DESCENDING, Optional.of(2), Optional.of(1));

        Assertions.assertEquals(2, films.size());

        Assertions.assertEquals(4L, films.getFirst().getId());
    }

    @Test
    void deleteFilm() {
        var filmService = new FilmService(
                new InMemoryFilmStorage(),
                new InMemoryUserStorage());

        filmService.postFilm(
                initFilmBuilder(1)
                        .name("film1")
                        .build());

        filmService.postFilm(
                initFilmBuilder(2)
                        .name("film2")
                        .build());

        filmService.postFilm(
                initFilmBuilder(3)
                        .name("film3")
                        .build());

        filmService.deleteFilm(2);

        Assertions.assertEquals(2, filmService.getFilms(SortOrder.UNKNOWN, Optional.empty(), Optional.empty()).size());
    }

    @Test
    void getThrowOnDeleteFilm() {
        var filmService = new FilmService(
                new InMemoryFilmStorage(),
                new InMemoryUserStorage());

        filmService.postFilm(
                initFilmBuilder(1)
                        .name("film1")
                        .build());

        filmService.postFilm(
                initFilmBuilder(2)
                        .name("film2")
                        .build());

        filmService.postFilm(
                initFilmBuilder(3)
                        .name("film3")
                        .build());

        Assertions.assertThrows(NotFoundException.class, () -> filmService.getFilm(5L));
    }

    @Test
    void updateNewFilm() {
        var filmService = new FilmService(
                new InMemoryFilmStorage(),
                new InMemoryUserStorage());

        filmService.postFilm(
                initFilmBuilder(1)
                        .name("film1")
                        .build());

        filmService.postFilm(
                initFilmBuilder(2)
                        .name("film2")
                        .build());

        Assertions.assertEquals(2, filmService.getFilms(SortOrder.UNKNOWN, Optional.empty(), Optional.empty()).size());
    }

    @Test
    void updateOldFilm() {
        var filmService = new FilmService(
                new InMemoryFilmStorage(),
                new InMemoryUserStorage());

        filmService.postFilm(
                initFilmBuilder(1)
                        .name("film1")
                        .build());

        filmService.postFilm(
                initFilmBuilder(1)
                        .name("film2")
                        .build());

        Assertions.assertEquals(1, filmService.getFilms(SortOrder.UNKNOWN, Optional.empty(), Optional.empty()).size());

        Assertions.assertEquals("film2", filmService.getFilm(1).getName());
    }

    @Test
    void setLikeFilm() {
        var userStorage = new InMemoryUserStorage();

        var filmService = new FilmService(
                new InMemoryFilmStorage(),
                userStorage);

        filmService.postFilm(
                initFilmBuilder(1)
                        .name("film1")
                        .build());

        Assertions.assertEquals(0, filmService.getFilm(1).getLikes().size(), "Начальное значение");

        for (int i = 1; i < 100; i++) {
           userStorage.createUser(initUserBuilder(i).build());

           filmService.like(1, i);

           Assertions.assertEquals(i, filmService.getFilm(1).getLikes().size());
        }
    }

    @Test
    void getThrowOnSetLikeFilm() {
        var filmService = new FilmService(
                new InMemoryFilmStorage(),
                new InMemoryUserStorage());

        filmService.postFilm(
                initFilmBuilder(1)
                        .name("film1")
                        .build());

        Assertions.assertThrows(NotFoundException.class, () -> filmService.like(2, 1L));
    }

    @Test
    void setDoubleLikeFilm() {
        var userStorage = new InMemoryUserStorage();

        var filmService = new FilmService(
                new InMemoryFilmStorage(),
                userStorage);

        userStorage.createUser(initUserBuilder(1).build());

        filmService.postFilm(
                initFilmBuilder(1)
                        .name("film1")
                        .build());

        filmService.like(1, 1L);

        Assertions.assertThrows(IdIsAlreadyInUseException.class, () -> filmService.like(1, 1L));
    }

    @Test
    void disLikeFilm() {
        var userStorage = new InMemoryUserStorage();

        var filmService = new FilmService(
                new InMemoryFilmStorage(),
                userStorage);

        filmService.postFilm(
                initFilmBuilder(1)
                        .name("film1")
                        .build());

        userStorage.createUser(initUserBuilder(1).build());
        userStorage.createUser(initUserBuilder(2).build());

        filmService.like(1, 1);
        filmService.like(1, 2);

        filmService.disLike(1, 1);

        Assertions.assertEquals(1, filmService.getFilm(1).getLikes().size());
    }

    @Test
    void getNotFoundFilmExceptionOnSetLikeFilmWhen0Likes() {
        var filmService = new FilmService(
                new InMemoryFilmStorage(),
                new InMemoryUserStorage());

        filmService.postFilm(
                initFilmBuilder(1)
                        .name("film1")
                        .build());

        Assertions.assertThrows(NotFoundException.class, () -> filmService.disLike(2, 1L));
    }

    @Test
    void getNotFoundFilmExceptionOnSetLikeFilmWhen3Likes() {
        var userStorage = new InMemoryUserStorage();

        var filmService = new FilmService(
                new InMemoryFilmStorage(),
                userStorage);

        filmService.postFilm(
                initFilmBuilder(1)
                        .name("film1")
                        .build());

        userStorage.createUser(initUserBuilder(1).build());
        userStorage.createUser(initUserBuilder(2).build());
        userStorage.createUser(initUserBuilder(4).build());

        filmService.like(1, 1);
        filmService.like(1, 2);
        filmService.like(1, 4);

        Assertions.assertThrows(NotFoundException.class, () -> filmService.disLike(2, 1L));
    }

    @Test
    void getNotFoundLikeExceptionOnSetLikeFilmWhen0Likes() {
        var filmService = new FilmService(
                new InMemoryFilmStorage(),
                new InMemoryUserStorage());

        filmService.postFilm(
                initFilmBuilder(1)
                        .name("film1")
                        .build());

        Assertions.assertThrows(NotFoundException.class, () -> filmService.disLike(1, 1L));
    }

    @Test
    void getNotFoundLikeExceptionOnSetLikeFilmWhen3Likes() {
        var userStorage = new InMemoryUserStorage();

        var filmService = new FilmService(
                new InMemoryFilmStorage(),
                userStorage);

        filmService.postFilm(
                initFilmBuilder(1)
                        .name("film1")
                        .build());

        userStorage.createUser(initUserBuilder(1).build());
        userStorage.createUser(initUserBuilder(2).build());
        userStorage.createUser(initUserBuilder(4).build());

        filmService.like(1, 1);
        filmService.like(1, 2);
        filmService.like(1, 4);

        Assertions.assertThrows(NotFoundException.class, () -> filmService.disLike(1, 5));
    }


    private Film.FilmBuilder initFilmBuilder(int id) {
        return Film.builder()
                .id((long) id)
                .name("name")
                .description("")
                .releaseDate(LocalDate.now())
                .duration(1);
    }

    private User.UserBuilder initUserBuilder(int id) {
        return User.builder()
                .id((long) id)
                .birthday(LocalDate.now());
    }
}
