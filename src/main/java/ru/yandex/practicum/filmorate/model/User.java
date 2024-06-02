package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.Value;
import ru.yandex.practicum.filmorate.tool.BirthDateUserConstraint;

import java.time.Instant;

@Value
public class User {
    long id;

    @NonNull
    @NotBlank
    @Email
    String email;

    @NonNull
    @NotBlank
    String login;

    String name;

    @BirthDateUserConstraint
    Instant birthDay;

    public String getName() {
        if (name == null || name.isBlank())
            return login;

        return name;
    }
}
