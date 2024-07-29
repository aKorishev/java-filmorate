package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IdIsAlreadyInUseException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.SortParameters;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUser(long userId) {
        if (!userStorage.containsKey(userId)) {
            throw new NotFoundException(String.format("Этот userId: %d не был найден", userId));
        }

        return userStorage.getUser(userId);
    }

    public List<User> getUsers(SortParameters parameters) {
        return userStorage.getUsers(parameters);
    }

    public List<User> getUnionFriends(List<Long> userIds) {
        Set<Long> friends = new HashSet<>();

        for (var userId: userIds) {
            if (!userStorage.containsKey(userId)) {
                throw new NotFoundException(String.format("Этот userId: %d не был найден", userId));
            }

            var curFriends = userStorage.getUser(userId).getFriends();

            log.trace(String.format("getIntersectFriends: userId %d содержит %d друзей", userId, curFriends.size()));

            if (curFriends.isEmpty()) {
                continue;
            }

            friends.addAll(curFriends
                    .stream().filter(i -> !friends.contains(i))
                    .collect(Collectors.toSet()));
        }

        return userStorage.getUsers(friends);
    }

    public List<User> getIntersectFriends(List<Long> userIds) {
        if (userIds.isEmpty()) return List.of();

        Set<Long> friends = new HashSet<>();

        for (var userId: userIds) {
            if (!userStorage.containsKey(userId)) {
                throw new NotFoundException(String.format("Этот userId: %d не был найден", userId));
            }

            if (friends.isEmpty()) {
                friends.addAll(userStorage.getUser(userId).getFriends());

                log.trace(String.format("getIntersectFriends: userId %d содержит %d друзей", userId, friends.size()));
            } else {
                var userFriends = userStorage
                        .getUser(userId)
                        .getFriends()
                        .stream()
                        .filter(friends::contains)
                        .collect(Collectors.toSet());

                if (userFriends.isEmpty()) {
                    return List.of();
                }

                friends = userFriends;

                log.trace(String.format("getIntersectFriends: пересечние с userId %d сохранило %d друзей", userId, friends.size()));
            }
        }

        return userStorage.getUsers(friends);
    }

    public User deleteUser(long userId) {
        if (!userStorage.containsKey(userId)) {
            throw new NotFoundException(String.format("Этот userId: %d не был найден", userId));
        }

        var oldUser = userStorage.getUser(userId);

        userStorage.deleteUser(userId);

        log.trace("Удалиил запись user: " + oldUser);

        return oldUser;
    }

    public User postUser(User user) {
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
        if (!userStorage.containsKey(userId)) {
            throw new NotFoundException("Не нашел userId " + userId);
        }
        if (!userStorage.containsKey(friendId)) {
            throw new NotFoundException("Не нашел friendId " + userId);
        }

        var user = userStorage.getUser(userId);

        BiFunction<User, Long, User> putFriend = (u, i) -> {
            var friends = new HashSet<>(u.getFriends());
            var uId = u.getId();

            if (uId.equals(i) || friends.contains(i))
                throw new IdIsAlreadyInUseException(String.format("User[%d] уже содержит друга %d", uId, i));

            friends.add(i);

            log.trace(String.format("putFriend: userId %d Добавил друга %d", u.getId(), i));

            return u.toBuilder()
                    .friends(friends)
                    .build();
        };

        var friend = putFriend.apply(userStorage.getUser(friendId), userId);
        userStorage.updateUser(friend);

        user = putFriend.apply(user, friendId);

        return userStorage.updateUser(user);
    }

    public User deleteFriend(long userId, long friendId) {
        if (!userStorage.containsKey(userId)) {
            throw new NotFoundException("Не нашел userId " + userId);
        }

        if (!userStorage.containsKey(friendId)) {
            throw new NotFoundException("Не нашел friendId " + userId);
        }

        BiFunction<User, Long, User> deleteFriend = (u, l) -> {
            var friends = u.getFriends().stream()
                    .filter(i -> !Objects.equals(i, l))
                    .collect(Collectors.toSet());

            log.trace(String.format("deleteFriend: userId %d удалил друга %d", u.getId(), l));

            return u.toBuilder()
                    .friends(friends)
                    .build();
        };

        var friend = deleteFriend.apply(
                userStorage.getUser(friendId),
                userId);
        userStorage.updateUser(friend);

        var user = deleteFriend.apply(
                userStorage.getUser(userId),
                friendId);

        return userStorage.updateUser(user);
    }
}
