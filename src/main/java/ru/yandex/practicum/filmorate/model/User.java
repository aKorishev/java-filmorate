package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.tool.BirthDateUserConstraint;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Value
@Builder(toBuilder = true)
@Validated
public class User {
    long id;

    @NotBlank
    @Email
    String email;

    @NotBlank
    String login;

    String name;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @BirthDateUserConstraint
    LocalDate birthday;

    Set<Long> friends;

    public String getName() {
        if (name == null || name.isBlank())
            return login;

        return name;
    }

    public Set<Long> getFriends() {
        if (friends == null)
            return new HashSet<>();
        return new HashSet<>(friends);
    }
}
