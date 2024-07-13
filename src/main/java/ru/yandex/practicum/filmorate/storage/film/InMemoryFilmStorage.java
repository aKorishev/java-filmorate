package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
    public void updateFilm(Film film) {
        films.replace(film.getId(), film);
    }

    @Override
    public void createFilm(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public void deleteFilm(long filmId) {
        films.remove(filmId);
    }
}
