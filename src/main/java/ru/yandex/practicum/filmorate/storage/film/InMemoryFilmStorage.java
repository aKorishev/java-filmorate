package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IdIsAlreadyInUseException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SortOrder;

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
    public List<Film> getFilms(SortOrder sortOrderLikes, Optional<Integer> size, Optional<Integer> from) {
        var stream = films.values().stream();

        if (sortOrderLikes == SortOrder.ASCENDING) {
            stream = stream.sorted(new Comparator<Film>() {
                @Override
                public int compare(Film o1, Film o2) {
                    return Integer.compare(o1.getLikes().size(), o2.getLikes().size());
                }
            });
        } else if (sortOrderLikes == SortOrder.DESCENDING) {
            stream = stream.sorted(new Comparator<Film>() {
                @Override
                public int compare(Film o1, Film o2) {
                    return -1 * Integer.compare(o1.getLikes().size(), o2.getLikes().size());
                }
            });
        }

        if (from.isPresent()) {
            stream = stream.skip(from.get());
        }

        if (size.isPresent()) {
            stream = stream.limit(size.get());
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
