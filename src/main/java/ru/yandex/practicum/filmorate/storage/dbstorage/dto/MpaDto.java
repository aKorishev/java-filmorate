package ru.yandex.practicum.filmorate.storage.dbstorage.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MpaDto {
    long mpaId;
    String name;
}
