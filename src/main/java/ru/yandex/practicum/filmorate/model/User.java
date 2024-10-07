package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.tool.BirthDateUserConstraint;

import java.time.LocalDate;


@Value
@Builder(toBuilder = true)
@Validated
public class User {
    Long id;

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

    public String getName() {
        if (name == null || name.isBlank())
            return login;

        return name;
    }
}
