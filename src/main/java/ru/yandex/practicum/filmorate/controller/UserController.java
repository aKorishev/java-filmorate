package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Storage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Storage storage;

    public UserController(Storage storage) {
        this.storage = storage;
    }

    @GetMapping
    public @ResponseBody List<User> getUsers() {
        var list =  storage.getUsers().values().stream().sorted(new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return Long.compare(o1.getId(), o2.getId());
                    }
                })
                .collect(Collectors.toList());
        return list;
    }

    @GetMapping("/{id}")
    public @ResponseBody User getUser(@PathVariable long id) throws Exception {
        var users = storage.getUsers();

        if (users.containsKey(id)) {
            return users.get(id);
        }

        throw new NotFoundException("Не нашел id = " + id);
    }

    @PostMapping
    public @ResponseBody User updateUser(@Valid @RequestBody User user) {
        return storage.updateUser(user);
    }

    @PutMapping
    public @ResponseBody User putUser(@Valid @RequestBody User user) throws Exception {
        if (storage.containsUser(user.getId())) {
            storage.updateUser(user);
            return user;
        }

        throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
    }
}
