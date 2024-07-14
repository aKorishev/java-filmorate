package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SortOrder;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film updateFilm(Film film);

    Film createFilm(Film film);

    Film deleteFilm(long filmId);

    boolean containsKey(long id);

    Film getFilm(long id);

    List<Film> getFilms(SortOrder sortOrderLikes, Optional<Integer> size, Optional<Integer> from);
}
