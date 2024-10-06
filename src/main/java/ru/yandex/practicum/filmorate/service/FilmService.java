package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.SortParameters;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;


@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(//todo перевести в yaml
                       @Qualifier("InDbFilmStorage") FilmStorage filmStorage,
                       @Qualifier("InDbUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film getFilm(long filmId) {
        log.trace(String.format("Request for Film by filmId - %d", filmId));

        if (!filmStorage.containsKey(filmId)) {
            throw new NotFoundException(String.format("Этот filmId: %d не был найден", filmId));
        }

        return filmStorage.getFilm(filmId);
    }

    public List<Film> getFilms(
            SortParameters parameters) {
        log.trace(String.format("Request for list of Films by \"%s\"", parameters));

        var films = filmStorage.getFilms(parameters);

        log.trace(String.format("Found %d films", films.size()));

        return films;
    }

    public Film putFilm(Film film) {
        log.trace(String.format("Request to put \"%s\"", film));

        var filmId = film.getId();

        if (filmId != null && filmStorage.containsKey(filmId)) {
            film = filmStorage.updateFilm(film);

            log.trace("putFilm: Обновил film: " + film);
        } else {
            if (filmId == null)
                throw new NotFoundException("filmId must be is not null");
            else
                throw new NotFoundException(String.format("filmId: %d не был найден", filmId));
        }

        return film;
    }

    public Film postFilm(Film film) {
        log.trace(String.format("Request to post \"%s\"", film));

        var filmId1 = film.getId();

        if (filmId1 != null && filmStorage.containsKey(filmId1)) {
            film = filmStorage.updateFilm(film);

            log.trace("postFilm: Обновил film: " + film);
        } else {
            film = filmStorage.createFilm(film);

            log.trace("postFilm: Создал film: " + film);
        }

        return film;
    }

    public Film deleteFilm(long filmId) {
        log.trace(String.format("Request to delete Film by filmId - %d", filmId));

        if (!filmStorage.containsKey(filmId)) {
            throw new NotFoundException(String.format("Этот filmId: %d не был найден", filmId));
        }

        var oldFilm = filmStorage.deleteFilm(filmId);

        log.trace("deleteFilm: Удалил film: " + oldFilm);

        return oldFilm;
    }

    public void like(long filmId, long userId) {
        log.trace(String.format("Request to like filmId - %d from userId - %d", filmId, userId));

        if (!filmStorage.containsKey(filmId)) {
            throw new NotFoundException(String.format("Этот filmId: %d не был найден", filmId));
        }
        if (!userStorage.containsKey(userId)) {
            throw new NotFoundException(String.format("Этот userId: %d не был найден", filmId));
        }

        filmStorage.like(filmId, userId);
    }

    public void disLike(long filmId, long userId) {
        log.trace(String.format("Request to dislike filmId - %d from userId - %d", filmId, userId));

        if (!filmStorage.containsKey(filmId)) {
            throw new NotFoundException(String.format("Этот filmId: %d не был найден", filmId));
        }

        filmStorage.disLike(filmId, userId);
    }

    public List<User> getLikes(long filmId) {
        List<Long> likes = filmStorage.getLikes(filmId);
        return userStorage.getUsers(new HashSet<>(likes));
    }
}
