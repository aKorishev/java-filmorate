package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import lombok.Value;
import ru.yandex.practicum.filmorate.tool.ReleaseDateFilmConstraint;

import java.time.Instant;

@Value
public class Film {
    long id;

    @NonNull
    @NotBlank
    String name;

    @Size(max = 200)
    String description;

    @NonNull
    @ReleaseDateFilmConstraint
    Instant releaseDate;

    @Min(1)
    long durationOfSeconds;
}
