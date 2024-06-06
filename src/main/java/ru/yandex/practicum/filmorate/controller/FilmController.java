package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Storage;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Storage storage;

    public FilmController(Storage storage) {
        this.storage = storage;
    }

    @GetMapping
    public @ResponseBody List<Film> getFilms() {
        return List.copyOf(storage.getFilms().values());
    }

    @GetMapping("/{id}")
    public @ResponseBody Film getFilm(@PathVariable long id) throws Exception {
        var films = storage.getFilms();

        if (films.containsKey(id))
            return films.get(id);

        throw new Exception("Не нашел id = " + id);
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

        throw new Exception("Пост с id = " + film.getId() + " не найден");
    }
}
