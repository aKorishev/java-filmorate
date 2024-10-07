package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IdIsAlreadyInUseException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.NotValidException;
import ru.yandex.practicum.filmorate.storage.SortParameters;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(
            @Qualifier("InDbUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUser(long userId) {
        log.trace(String.format("Request for User by userId - %d", userId));

        if (!userStorage.containsKey(userId)) {
            throw new NotFoundException(String.format("Этот userId: %d не был найден", userId));
        }

        log.trace(String.format("User found by userId - %d", userId));

        return userStorage.getUser(userId);
    }

    public List<User> getUsers(SortParameters parameters) {
        log.trace(String.format("Request for list of users by %s", parameters));

        return userStorage.getUsers(parameters);
    }

    public List<User> getUnionFriends(List<Long> userIds) {
        log.trace(String.format("Request for list of union friends by %15s", userIds));
        return userStorage.getUnionFriends(new HashSet<>(userIds));
    }

    public List<User> getIntersectFriends(List<Long> userIds) {
        log.trace(String.format("Request for list of intersect friends by %15s", userIds));

        return userStorage.getIntersectFriends(new HashSet<>(userIds));
    }

    public List<User> getFriends(long userId) {
        log.trace(String.format("Request for list of friends by %d", userId));

        if (!userStorage.containsKey(userId)) {
            throw new NotFoundException(String.format("Этот userId: %d не был найден", userId));
        }

        return userStorage.getUnionFriends(Set.of(userId));
    }

    public User deleteUser(long userId) {
        log.trace(String.format("Request to delete user by %d", userId));

        if (!userStorage.containsKey(userId)) {
            throw new NotFoundException(String.format("Этот userId: %d не был найден", userId));
        }

        var oldUser = userStorage.getUser(userId);

        userStorage.deleteUser(userId);

        log.trace("Удалиил запись user: " + oldUser);

        return oldUser;
    }

    public User postUser(User user) {
        log.trace(String.format("Request to post \"%s\"", user));

        var userId = user.getId();

        if (userId != null && userStorage.containsKey(userId)) {
            user = userStorage.updateUser(user);

            log.trace("postUser: Обновил user: " + user);
        } else {
            user = userStorage.createUser(user);

            log.trace("postUser: Создал user: " + user);
        }

        return user;
    }

    public User putUser(User user) {
        log.trace(String.format("Request to put \"%s\"", user));

        var userId = user.getId();

        if (userId != null && userStorage.containsKey(userId)) {
            user = userStorage.updateUser(user);

            log.trace("putUser: Обновил user: " + user);
        } else {
            if (userId == null)
                throw new NotFoundException("putUser: userId: null не был найден");
            else
                throw new NotFoundException(String.format("putUser: userId: %d не был найден", userId));
        }

        return user;
    }

    public User putFriend(long userId, long friendId) {
        log.trace(String.format("Request to put friendId %d to user %d", friendId, userId));

        if (userId == friendId) {
            throw new IdIsAlreadyInUseException("Пользователь не может дружить сам с собой");
        }

        if (!userStorage.containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }

        if (!userStorage.containsKey(friendId)) {
            throw new NotFoundException("Друг не найден в списке пользователей");
        }

        if (!userStorage.putFriend(userId, friendId)) {
            throw new NotValidException("Ошибка при добавлении friend");
        }

        return userStorage.getUser(userId);
    }

    public User deleteFriend(long userId, long friendId) {
        log.trace(String.format("Request to delete friendId %d from user %d", friendId, userId));

        if (userId == friendId) {
            throw new IdIsAlreadyInUseException("Пользователь не может дружить сам с собой");
        }

        if (!userStorage.containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }

        if (!userStorage.containsKey(friendId)) {
            throw new NotFoundException("Друг не найден в списке пользователей");
        }

        userStorage.deleteFriend(userId, friendId);

        return userStorage.getUser(userId);
    }
}
