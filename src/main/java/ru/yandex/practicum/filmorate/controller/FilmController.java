package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Storage;
import ru.yandex.practicum.filmorate.model.User;

import javax.swing.plaf.ActionMapUIResource;
import java.security.PublicKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Storage storage;

    public FilmController(Storage storage){
        this.storage = storage;
    }

    @GetMapping
    public @ResponseBody List<Film> getFilms(){
        return List.copyOf(storage.getFilms().values());
    }

    @GetMapping("/{id}")
    public @ResponseBody Film getFilm(@Valid @RequestBody long id) throws Exception {
        var films = storage.getFilms();

        if (films.containsKey(id))
            return films.get(id);

        throw new Exception("Не нашел id = " + id);
    }

    @PostMapping
    public @ResponseBody Film updateFilm(@Valid @RequestBody Film film) {
        storage.updateFilm(film);

        return film;
    }
}
