package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public @ResponseBody List<Genre> getGenres() {
        return genreService.getGenres();
    }

    @GetMapping("/{genreId}")
    public @ResponseBody Genre getGenre(@PathVariable long genreId) {
       return genreService.getGenre(genreId);
    }

    @PostMapping
    public @ResponseBody Genre postGenre(@Valid @RequestBody Genre genre) {
        return genreService.postGenre(genre);
    }

    @PutMapping
    public @ResponseBody Genre putGenre(@Valid @RequestBody Genre genre) {
        return genreService.putGenre(genre);
    }
}
