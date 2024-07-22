package ru.yandex.practicum.filmorate.storage.film;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IdIsAlreadyInUseException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SortOrder;
import ru.yandex.practicum.filmorate.model.SortParameters;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    Map<Long, Film> films = new HashMap<>();

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
            stream = stream.sorted(Comparator.comparingInt(o -> o.getLikes().size()));
        } else if (parameters.getSortOrder() == SortOrder.DESCENDING) {
            stream = stream.sorted(Comparator.comparingInt(o -> -1 * o.getLikes().size()));
        }

        if (parameters.getFrom().isPresent()) {
            stream = stream.skip(parameters.getFrom().get());
        }

        if (parameters.getSize().isPresent()) {
            stream = stream.limit(parameters.getSize().get());
        }

        return stream.collect(Collectors.toList());
    }

    @Override
    public Film updateFilm(Film film) {
        films.replace(film.getId(), film);

        return film;
    }

    @Override
    public Film createFilm(Film film) {
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

    private long getIncrementId() {
        return films.keySet()
                .stream().mapToLong(i -> i)
                .max()
                .orElse(0) + 1;
    }
}
