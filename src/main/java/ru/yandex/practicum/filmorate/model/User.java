package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.Value;
import ru.yandex.practicum.filmorate.tool.BirthDateUserConstraint;

import java.time.LocalDate;


@Value
public class User {
    long id;

    @NotBlank
    @Email
    String email;

    @NotBlank
    @NonNull
    String login;

    String name;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    //@JsonDeserialize(using = LocalDateDeserializer.class)
    @BirthDateUserConstraint
    LocalDate birthday;

    public String getName() {
        if (name == null || name.isBlank())
            return login;

        return name;
    }
}
