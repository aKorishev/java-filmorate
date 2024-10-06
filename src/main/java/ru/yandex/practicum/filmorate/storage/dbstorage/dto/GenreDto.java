package ru.yandex.practicum.filmorate.storage.dbstorage.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GenreDto {
    long genreId;
    String name;
}
