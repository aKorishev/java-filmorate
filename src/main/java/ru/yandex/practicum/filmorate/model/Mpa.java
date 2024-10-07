package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import org.springframework.validation.annotation.Validated;

@Value
@Validated
@Builder(toBuilder = true)
public class Mpa {
    Long id;

    @NotBlank
    String name;
}
