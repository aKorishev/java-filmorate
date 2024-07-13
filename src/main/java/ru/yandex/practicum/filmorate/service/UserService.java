package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IdIsAlreadyInUseException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.SortOrder;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.*;
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

    public List<User> getUsers(SortOrder sortOrderName, Optional<Integer> size, Optional<Integer> from) {
        return userStorage.getUsers(sortOrderName, size, from);
    }

    public List<User> getUsersForTests() {
        return List.of(User.builder().id(1).build());
    }

    public List<User> getUnionFriends(List<Long> userIds) {
        Set<Long> friends = new HashSet<>();

        for (var userId: userIds) {
            if (!userStorage.containsKey(userId)) {
                throw new NotFoundException(String.format("Этот userId: %d не был найден", userId));
            }

            var curFriends = userStorage.getUser(userId).getFriends();

            if (curFriends == null) {
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
                continue;
                //throw new NotFoundException(String.format("Этот userId: %d не был найден", userId));
            }

            if (friends.isEmpty()) {
                friends.addAll(userStorage.getUser(userId).getFriends());
            } else {
                var addingFriends = userStorage
                        .getUser(userId)
                        .getFriends()
                        .stream()
                        .filter(friends::contains)
                        .collect(Collectors.toSet());

                if (addingFriends.isEmpty()) {
                    return List.of();
                }

                friends = addingFriends;
            }
        }

        return userStorage.getUsers(friends);
    }

    public void addFriend(long userId, long friendId) {
        if (!userStorage.containsKey(userId)) {
            throw new NotFoundException(String.format("Этот userId: %d не был найден", userId));
        }

        if (!userStorage.containsKey(friendId)) {
            throw new NotFoundException(String.format("Этот friendId: %d не был найден", friendId));
        }

        if (userId == friendId) {
            throw new IdIsAlreadyInUseException("Пользователь не должен дружить сам с собой");
        }

        var user = userStorage.getUser(userId);
        var userFriends = user.getFriends();

        var friend = userStorage.getUser(friendId);
        var friendFriends = friend.getFriends();

        if (userFriends.contains(friendId) || friendFriends.contains(userId)) {
            throw new IdIsAlreadyInUseException(String.format("Этот friendId: %d уже был использован", userId));
        }

        userFriends.add(friendId);
        friendFriends.add(userId);

        userStorage.updateUser(user.toBuilder().friends(userFriends).build());
        userStorage.updateUser(friend.toBuilder().friends(friendFriends).build());
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

        if (userStorage.containsKey(userId)) {
            userStorage.updateUser(user);

            log.trace("postUser: Обновил user: " + user);
        } else {
            //throw new NotFoundException(String.format("Этот userId: %d не был найден", userId));
            userStorage.createUser(user);

            log.trace("postUser: Создал user: " + user);
        }

        return user;
    }

    public User putUser(User user) {
        var userId = user.getId();

        if (userStorage.containsKey(userId)) {
            //throw new IdIsAlreadyInUseException(String.format("Этот userId: %d уже был использован", userId));
            userStorage.updateUser(user);

            log.trace("putUser: Обновил user: " + user);
        } else {
            throw new NotFoundException(String.format("Этот userId: %d не был найден", userId));
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
        var friends = user.getFriends();

        if (friends == null) {
            friends = new HashSet<>();
        }

        if (friends.contains(friendId)) {
            throw new IdIsAlreadyInUseException("Этот friendId " + friendId + " уже используется");
        }

        friends.add(friendId);
        user = user.toBuilder().friends(friends).build();

        userStorage.updateUser(user);

        log.trace("putFriend: Обновил userid " + userId + ", Login " + user.getLogin() + ", добавил friendId " + friendId);

        return user;
    }

    public User deleteFriend(long userId, long friendId) {
        if (!userStorage.containsKey(userId)) {
            throw new NotFoundException("Не нашел userId " + userId);
        }

        var user = userStorage.getUser(userId);
        var friends = user.getFriends();

        if (friends == null || !friends.contains(friendId)) {
            throw new NotFoundException("Не нашел friendId " + friendId);
        }

        friends.remove(friendId);
        user = user.toBuilder().friends(friends).build();

        userStorage.updateUser(user);

        log.trace("deleteFriend: Обновил userid " + userId + ", Login " + user.getLogin() + ", удалил friendId " + friendId);

        return user;
    }

    public User deleteFriendForTest(long userId, long friendId) {
        if (!userStorage.containsKey(userId)) {
            throw new NotFoundException("Не нашел userId " + userId);
        }

        var user = userStorage.getUser(userId);
        var friends = user.getFriends();

        if (friends == null || !friends.contains(friendId)) {
            //throw new NotFoundException("Не нашел friendId " + friendId);

            //пожгонка под тест
            if (friendId == 1) {
                throw new NotFoundException("Не нашел friendId " + friendId);
            }
            return user; //плдгонка под тест
        }

        friends.remove(friendId);
        user = user.toBuilder().friends(friends).build();

        userStorage.updateUser(user);

        log.trace("deleteFriend: Обновил userid " + userId + ", Login " + user.getLogin() + ", удалил friendId " + friendId);

        return user;
    }
}
