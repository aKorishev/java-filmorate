package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IdIsAlreadyInUseException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SortParameters;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film getFilm(long filmId) {
        if (!filmStorage.containsKey(filmId)) {
            throw new NotFoundException(String.format("Этот filmId: %d не был найден", filmId));
        }

        return filmStorage.getFilm(filmId);
    }

    public List<Film> getFilms(
            SortParameters parameters) {
        return filmStorage.getFilms(parameters);
    }

    public Film putFilm(Film film) {
        var filmId = film.getId();

        if (filmId != null && filmStorage.containsKey(filmId)) {
            film = filmStorage.updateFilm(film);

            log.trace("putFilm: Обновил film: " + film);
        } else {
            if (filmId == null)
                throw new NotFoundException("putFilm: filmId: null не был найден");
            else
                throw new NotFoundException(String.format("putFilm: filmId: %d не был найден", filmId));
        }

        return film;
    }

    public Film postFilm(Film film) {
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
        if (!filmStorage.containsKey(filmId)) {
            throw new NotFoundException(String.format("Этот filmId: %d не был найден", filmId));
        }

        var oldFilm = filmStorage.deleteFilm(filmId);

        log.trace("deleteFilm: Удалил film: " + oldFilm);

        return oldFilm;
    }

    public void like(long filmId, long userId) {
        if (!filmStorage.containsKey(filmId)) {
            throw new NotFoundException(String.format("Этот filmId: %d не был найден", filmId));
        }
        if (!userStorage.containsKey(userId)) {
            throw new NotFoundException(String.format("Этот userId: %d не был найден", filmId));
        }

        var film = filmStorage.getFilm(filmId);

        Set<Long> likes = new HashSet<>(filmStorage.getFilm(filmId).getLikes());

        if (likes.contains(userId)) {
            throw new IdIsAlreadyInUseException("Лайк от этого пользователя уже есть");
        }

        likes.add(userId);

        filmStorage.updateFilm(
                film.toBuilder()
                        .likes(likes)
                        .build());
    }

    public void disLike(long filmId, long userId) {
        if (!filmStorage.containsKey(filmId)) {
            throw new NotFoundException(String.format("Этот filmId: %d не был найден", filmId));
        }

        var film = filmStorage.getFilm(filmId);

        var likes = film.getLikes();

        if (!likes.contains(userId)) {
            throw new NotFoundException("Лайк от этого пользователя отсутсвует");
        }

        filmStorage.updateFilm(
                film.toBuilder()
                        .likes(likes.stream()
                                .filter(i -> i != userId)
                                .collect(Collectors.toSet()))
                        .build());
    }
}
