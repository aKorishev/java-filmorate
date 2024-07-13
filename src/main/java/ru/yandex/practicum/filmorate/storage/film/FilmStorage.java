package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SortOrder;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    void updateFilm(Film film);

    void createFilm(Film film);

    void deleteFilm(long filmId);

    boolean containsKey(long id);

    Film getFilm(long id);

    public List<Film> getFilms(SortOrder sortOrderLikes, Optional<Integer> size, Optional<Integer> from);
}
