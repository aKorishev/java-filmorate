package ru.yandex.practicum.filmorate.storage.film;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SortParameters;

import java.util.List;

public interface FilmStorage {
    Film updateFilm(Film film);

    Film createFilm(Film film);

    Film deleteFilm(long filmId);

    boolean containsKey(long id);

    Film getFilm(long id);

    List<Film> getFilms(@NonNull SortParameters parameters);
}
