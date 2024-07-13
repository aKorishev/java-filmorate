package ru.yandex.practicum.filmorate.model;

import lombok.Value;

@Value
public class ErrorResponse {
    public String error;
    public String description;
}
