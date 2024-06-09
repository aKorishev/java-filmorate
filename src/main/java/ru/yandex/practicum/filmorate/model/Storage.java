package ru.yandex.practicum.filmorate.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class Storage {
    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Long, User> users = new HashMap<>();

    public Map<Long, Film> getFilms() {
        return Map.copyOf(films);
    }

    public Map<Long, User> getUsers() {
        return Map.copyOf(users);
    }

    public Film updateFilm(Film film) {
        var id = film.getId();

        if (id != 0 && containsFilm(id)) {
            films.replace(id, film);
            log.debug("Updated film id = " + id);
        } else {
            if (id == 0) {
                id = films.keySet().stream()
                        .max(Long::compareTo)
                        .orElse(0L)
                        + 1;

                film = new Film(id,
                        film.getName(),
                        film.getDescription(),
                        film.getReleaseDate(),
                        film.getDuration());
            }

            films.put(id, film);

            log.debug("Putted film id = " + id);
        }

        return film;
    }

    public boolean containsFilm(Long id) {
        return films.containsKey(id);
    }

    public Film getFilm(Long id) {
        return films.get(id);
    }

    public User updateUser(User user) {
        var id = user.getId();

        if (id != 0 && containsFilm(id)) {
            users.replace(id, user);
            log.debug("Updated user id = " + id);
        } else {
            if (id == 0) {
                id = users.keySet().stream().max(Long::compareTo).orElse(0L) + 1;

                user = new User(id,
                        user.getEmail(),
                        user.getLogin(),
                        user.getName(),
                        user.getBirthday());
            }

            users.put(id, user);
            log.debug("Putted user id = " + id);
        }

        return user;
    }

    public boolean containsUser(Long id) {
        return users.containsKey(id);
    }

    public User getUser(Long id) {
        return users.get(id);
    }
}
