package ru.yandex.practicum.filmorate.storage;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmStorage {
    Film updateFilm(Film film);

    Film createFilm(Film film);

    Film deleteFilm(long filmId);

    boolean containsKey(long id);

    Film getFilm(long id);

    List<Film> getFilms(@NonNull SortParameters parameters);

    void like(long filmId, long userId);

    void disLike(long filmId, long userId);

    List<Long> getLikes(long filmId);

    List<Genre> getGenres();

    Genre findGenreById(long genreId);

    Genre updateGenre(Genre genre);

    Genre createGenre(Genre genre);

    Genre deleteGenre(long genreId);

    List<Mpa> getMpas();

    Mpa findMpaById(long ratingId);

    Mpa updateMpa(Mpa rating);

    Mpa createRating(Mpa rating);

    Mpa deleteMpa(long ratingId);

    boolean containsMpaKey(long mpaId);

    boolean containsGenreIdKey(long genreId);
}
