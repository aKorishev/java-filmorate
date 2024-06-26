package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Storage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Storage storage;

    public FilmController(Storage storage) {
        this.storage = storage;
    }

    @GetMapping
    public @ResponseBody List<Film> getFilms() {
        var list =  storage.getFilms().values().stream()
                .sorted(new Comparator<Film>() {
                    @Override
                    public int compare(Film o1, Film o2) {
                        return Long.compare(o1.getId(), o2.getId());
                    }
                })
                .collect(Collectors.toList());
        return list;
    }

    @GetMapping("/{id}")
    public @ResponseBody Film getFilm(@PathVariable long id) throws Exception {
        var films = storage.getFilms();

        if (films.containsKey(id)) {
            return films.get(id);
        }

        throw new NotFoundException("Не нашел id = " + id);
    }

    @PostMapping
    public @ResponseBody Film updateFilm(@Valid @RequestBody Film film) {
        return storage.updateFilm(film);
    }

    @PutMapping
    public @ResponseBody Film putFilm(@Valid @RequestBody Film film) throws Exception {
        if (storage.containsFilm(film.getId())) {
            storage.updateFilm(film);
            return film;
        }

        throw new NotFoundException("Пост с id = " + film.getId() + " не найден");
    }
}
