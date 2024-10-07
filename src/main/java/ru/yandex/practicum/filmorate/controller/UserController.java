package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.storage.SortParameters;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public @ResponseBody List<User> getUsers() {
        return userService.getUsers(SortParameters.builder().build());
    }

    @GetMapping("/{userId}")
    public @ResponseBody User getUser(@PathVariable long userId) {
        return userService.getUser(userId);
    }

    @PostMapping
    public @ResponseBody User postUser(@Valid @RequestBody User user) {
        return userService.postUser(user);
    }

    @PutMapping
    public @ResponseBody User putUser(@Valid @RequestBody User user) {
        return userService.putUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public @ResponseBody User putFriend(@PathVariable long userId, @PathVariable long friendId) {
        return userService.putFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public @ResponseBody User deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public @ResponseBody List<User> getFriends(@PathVariable long userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    public @ResponseBody List<User> getIntersectFriends(@PathVariable long userId, @PathVariable long otherUserId) {
        return userService.getIntersectFriends(List.of(userId, otherUserId));
    }
}
