package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Storage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Storage storage;

    public UserController(Storage storage) {
        this.storage = storage;
    }

    @GetMapping
    public @ResponseBody List<User> getUsers() {
        return List.copyOf(storage.getUsers().values());
    }

    @GetMapping("/{id}")
    public @ResponseBody User getUser(@Valid @RequestBody long id) throws Exception {
        var users = storage.getUsers();

        if (users.containsKey(id))
            return users.get(id);

        throw new Exception("Не нашел id = " + id);
    }

    @PostMapping
    public @ResponseBody User updateUser(@Valid @RequestBody User user) {
        storage.updateUser(user);

        return user;
    }
}
