package ru.yandex.practicum.filmorate.storage.dbstorage.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
public class FilmDto {
    Long filmId;
    String name;
    String description;
    LocalDate releaseDate;
    long duration;
    long mpaId;
    String mpaName;
}
