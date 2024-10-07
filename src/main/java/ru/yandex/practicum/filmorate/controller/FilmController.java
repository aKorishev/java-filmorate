package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SortOrder;
import ru.yandex.practicum.filmorate.storage.SortParameters;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public @ResponseBody List<Film> getFilms() {
        return filmService.getFilms(SortParameters.builder().build());
    }

    @GetMapping("/{filmId}")
    public @ResponseBody Film getFilm(@PathVariable long filmId) {
       return filmService.getFilm(filmId);
    }

    @PostMapping
    public @ResponseBody Film postFilm(@Valid @RequestBody Film film) {
        return filmService.postFilm(film);
    }

    @PutMapping
    public @ResponseBody Film putFilm(@Valid @RequestBody Film film) {
        return filmService.putFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public @ResponseBody Film putLike(@PathVariable long filmId, @PathVariable long userId) {
        filmService.like(filmId, userId);

        return filmService.getFilm(filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public @ResponseBody Film deleteLike(@PathVariable long filmId, @PathVariable long userId) {
        filmService.disLike(filmId, userId);

        return filmService.getFilm(filmId);
    }

    @GetMapping("/popular")
    public @ResponseBody List<Film> getFilms(
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> skip) {

        return filmService.getFilms(
                SortParameters.builder()
                        .sortOrder(SortOrder.DESCENDING)
                        .size(limit.orElse(10))
                        .from(skip)
                        .build());
    }
}
