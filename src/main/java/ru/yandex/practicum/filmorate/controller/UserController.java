package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.SortOrder;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public @ResponseBody List<User> getUsers() {
        return userService.getUsers(
                SortOrder.UNKNOWN,
                Optional.empty(),
                Optional.empty());
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
        return userService.getUnionFriends(List.of(userId));
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    public @ResponseBody List<User> getIntersectFriends(@PathVariable long userId, @PathVariable long otherUserId) {
        return userService.getIntersectFriends(List.of(userId, otherUserId));
    }
}
