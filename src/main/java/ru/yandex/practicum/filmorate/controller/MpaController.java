package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public @ResponseBody List<Mpa> getRatings() {
        return mpaService.getRatings();
    }

    @GetMapping("/{id}")
    public @ResponseBody Mpa getRating(@PathVariable long id) {
       return mpaService.getRating(id);
    }

    @PostMapping
    public @ResponseBody Mpa postRating(@Valid @RequestBody Mpa mpa) {
        return mpaService.postMpa(mpa);
    }

    @PutMapping
    public @ResponseBody Mpa putRating(@Valid @RequestBody Mpa mpa) {
        return mpaService.putRating(mpa);
    }
}
