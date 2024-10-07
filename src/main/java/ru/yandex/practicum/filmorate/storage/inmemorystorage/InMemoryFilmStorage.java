package ru.yandex.practicum.filmorate.storage.inmemorystorage;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IdIsAlreadyInUseException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.NotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.SortOrder;
import ru.yandex.practicum.filmorate.storage.SortParameters;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@Qualifier("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    Map<Long, Film> films = new HashMap<>();
    Map<Long, Set<Long>> likes = new HashMap<>();
    Map<Long, Mpa> mpas = new HashMap<>();
    Map<Long, Genre> genres = new HashMap<>();

    @Override
    public boolean containsKey(long filmId) {
        return films.containsKey(filmId);
    }

    @Override
    public Film getFilm(long filmId) {
        return films.get(filmId);
    }

    @Override
    public List<Film> getFilms(
            @NonNull SortParameters parameters) {
        var stream = films.values().stream();

        if (parameters.getSortOrder() == SortOrder.ASCENDING) {
            stream = stream.sorted(Comparator.comparingInt(f -> {
                var id = f.getId();
                if (!likes.containsKey(id))
                    return 0;
                else
                    return likes.get(id).size();
                    }));
        } else if (parameters.getSortOrder() == SortOrder.DESCENDING) {
            stream = stream.sorted(Comparator.comparingInt(f -> {
                var id = f.getId();
                if (!likes.containsKey(id))
                    return 0;
                else
                    return -1 * likes.get(id).size();
            }));
        }

        if (parameters.getFrom().isPresent()) {
            stream = stream.skip(parameters.getFrom().get());
        }

        if (parameters.getSize().isPresent()) {
            stream = stream.limit(parameters.getSize().get());
        }

        return stream.collect(Collectors.toList());
    }

    private boolean containsGenre(Genre genre) {
        return genres.containsKey(genre.getId());
    }

    private void checkContainsReferences(Film film) {
        var mpa = film.getMpa();

        if (mpa != null && !containsMpaKey(mpa.getId())) {
            throw  new NotValidException("Не найден mpa_id " + mpa.getId());
        }

        if (film.getGenres() != null) {
            for (var genre : film.getGenres()) {
                if (!containsGenre(genre)) {
                    throw new NotValidException("Не найден genre_id " + genre.getId());
                }
            }
        }
    }

    @Override
    public Film updateFilm(Film film) {
        checkContainsReferences(film);

        films.replace(film.getId(), film);

        return film;
    }

    @Override
    public Film createFilm(Film film) {
        checkContainsReferences(film);

        var filmId = film.getId();

        if (filmId == null) {
            filmId = getIncrementId();

            film = film.toBuilder().id(filmId).build();
        } else if (films.containsKey(filmId)) {
            throw new IdIsAlreadyInUseException(String.format("Этот film: %d уже был использован", filmId));
        }

        films.put(filmId, film);

        return film;
    }

    @Override
    public Film deleteFilm(long filmId) {
        if (films.containsKey(filmId)) {
            var film = films.get(filmId);

            films.remove(filmId);

            return film;
        }

        return null;
    }

    @Override
    public void like(long filmId, long userId) {
        if (!likes.containsKey(filmId)) {
            var likes = new HashSet<Long>();
            likes.add(userId);
            this.likes.put(filmId, likes);

            return;
        }

        var users = likes.get(filmId);

        if (users.contains(userId)) {
            throw new IdIsAlreadyInUseException("Лайк от этого пользователя уже есть");
        }

        users.add(userId);
    }

    @Override
    public void disLike(long filmId, long userId) {
        if (!likes.containsKey(filmId)) {
            throw new NotFoundException("Лайк от этого пользователя отсутсвует");
        }

        var users = likes.get(filmId);

        if (!users.contains(userId)) {
            throw new NotFoundException("Лайк от этого пользователя отсутсвует");
        }

        users.remove(userId);
    }

    @Override
    public List<Long> getLikes(long filmId) {
        if (!likes.containsKey(filmId)) {
            return List.of();
        }

        return new ArrayList<>(likes.get(filmId));
    }

    @Override
    public List<Genre> getGenres() {
        return genres.values().stream().toList();
    }

    @Override
    public Genre findGenreById(long genreId) {
        if (!genres.containsKey(genreId)) {
            throw new NotFoundException(String.format("Этот genreId: %d не был найден", genreId));
        }

        return genres.get(genreId);
    }

    @Override
    public Genre updateGenre(Genre genre) {
        genres.replace(genre.getId(), genre);

        return genre;
    }

    @Override
    public Genre createGenre(Genre genre) {
        var genreId = genre.getId();

        if (genreId == null) {
            genreId = getIncrementId();

            genre = genre.toBuilder().id(genreId).build();
        } else if (films.containsKey(genreId)) {
            throw new IdIsAlreadyInUseException(String.format("Этот genre: %d уже был использован", genreId));
        }

        genres.put(genreId, genre);

        return genre;
    }

    @Override
    public Genre deleteGenre(long genreId) {
        if (genres.containsKey(genreId)) {
            var genre = genres.get(genreId);

            genres.remove(genreId);

            return genre;
        }

        return null;
    }

    @Override
    public List<Mpa> getMpas() {
        return mpas.values().stream().toList();
    }

    @Override
    public Mpa findMpaById(long ratingId) {
        if (!mpas.containsKey(ratingId)) {
            throw new NotFoundException(String.format("Этот ratingId: %d не был найден", ratingId));
        }

        return mpas.get(ratingId);
    }

    @Override
    public Mpa updateMpa(Mpa rating) {
        mpas.replace(rating.getId(), rating);

        return rating;
    }

    @Override
    public Mpa createRating(Mpa rating) {
        var ratingId = rating.getId();

        if (ratingId == null) {
            ratingId = getIncrementId();

            rating = rating.toBuilder().id(ratingId).build();
        } else if (films.containsKey(ratingId)) {
            throw new IdIsAlreadyInUseException(String.format("Этот genre: %d уже был использован", ratingId));
        }

        mpas.put(ratingId, rating);

        return rating;
    }

    @Override
    public Mpa deleteMpa(long ratingId) {
        if (mpas.containsKey(ratingId)) {
            var rating = mpas.get(ratingId);

            mpas.remove(ratingId);

            return rating;
        }

        return null;
    }

    @Override
    public boolean containsMpaKey(long mpaId) {
        return mpas.containsKey(mpaId);
    }

    @Override
    public boolean containsGenreIdKey(long genreId) {
        return genres.containsKey(genreId);
    }

    private long getIncrementId() {
        return films.keySet()
                .stream().mapToLong(i -> i)
                .max()
                .orElse(0) + 1;
    }
}
