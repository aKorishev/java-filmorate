package ru.yandex.practicum.filmorate.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.tool.ReleaseDateFilmConstraint;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Value
@Validated
@Builder(toBuilder = true)
public class Film {
    Long id;

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

    Set<Long> likes;

    public Set<Long> getLikes() {
        if (likes == null) {
            return new HashSet<>();
        }

        return Set.copyOf(likes);
    }
}
