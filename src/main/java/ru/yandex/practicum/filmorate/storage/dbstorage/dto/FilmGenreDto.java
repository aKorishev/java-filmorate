package ru.yandex.practicum.filmorate.storage.dbstorage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FilmGenreDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long filmId;
    long genreId;
}
