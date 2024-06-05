package ru.yandex.practicum.filmorate.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import lombok.Value;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.tool.ReleaseDateFilmConstraint;

import java.time.LocalDate;

@Value
@Validated
public class Film {
    long id;

    @NotBlank
    String name;

    @Size(max = 200)
    String description;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ReleaseDateFilmConstraint
    LocalDate releaseDate;

    @Min(1)
    long duration;
}
