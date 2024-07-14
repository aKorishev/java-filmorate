package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IdIsAlreadyInUseException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SortOrder;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
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

    public List<Film> getFilms(SortOrder sortOrderLikes, Optional<Integer> size, Optional<Integer> from) {
        return filmStorage.getFilms(sortOrderLikes, size, from);
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

        film = film.toBuilder()
                .likes(likes)
                .build();

        postFilm(film);
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

        likes.remove(userId);

        film = film.toBuilder()
                .likes(likes)
                .build();

        postFilm(film);
    }
}
