package ru.yandex.practicum.filmorate.storage.inmemorystorage;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IdIsAlreadyInUseException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.SortOrder;
import ru.yandex.practicum.filmorate.storage.SortParameters;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    Map<Long, User> users = new HashMap<>();
    Map<Long, Set<Long>> friends = new HashMap<>();

    @Override
    public boolean containsKey(long userId) {
        return users.containsKey(userId);
    }

    @Override
    public User getUser(long userId) {
        return users.get(userId);
    }

    @Override
    public List<User> getUsers(
            @NonNull SortParameters parameters) {
        var stream = users.values().stream();

        if (parameters.getSortOrder() == SortOrder.ASCENDING) {
            stream = stream.sorted(Comparator.comparing(User::getName));
        } else if (parameters.getSortOrder() == SortOrder.DESCENDING) {
            stream = stream.sorted((o1, o2) -> -1 * o1.getName().compareTo(o2.getName()));
        }

        if (parameters.getFrom().isPresent()) {
            stream = stream.skip(parameters.getFrom().get());
        }

        if (parameters.getSize().isPresent()) {
            stream = stream.limit(parameters.getSize().get());
        }

        return stream.collect(Collectors.toList());
    }

    @Override
    public User updateUser(User user) {
        users.replace(user.getId(), user);

        return user;
    }

    @Override
    public User createUser(User user) {
        var userId = user.getId();

        if (userId == null) {
            userId = getIncrementId();

            user = user.toBuilder().id(userId).build();
        } else if (users.containsKey(userId)) {
            throw new IdIsAlreadyInUseException(String.format("Этот userId: %d уже был использован", userId));
        }

        users.put(userId, user);

        return user;
    }

    @Override
    public User deleteUser(long userId) {
        if (users.containsKey(userId)) {
            var user = users.get(userId);

            users.remove(userId);

            return user;
        }

        return null;
    }

    @Override
    public List<User> getUsers(Set<Long> ids) {
        return ids.stream()
                .filter(users::containsKey)
                .map(i -> users.get(i))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getUnionFriends(Set<Long> ids) {
        Set<Long> friends = new HashSet<>();

        for (var userId: ids) {
            if (!users.containsKey(userId)) {
                throw new NotFoundException(String.format("Этот userId: %d не был найден", userId));
            }

            if (this.friends.containsKey(userId)) {
                friends.addAll(this.friends.get(userId));
            }
        }

        return friends.stream()
                .map(users::get)
                .toList();
    }

    @Override
    public List<User> getIntersectFriends(Set<Long> ids) {
        Set<Long> friends = new HashSet<>();

        for (var userId: ids) {
            if (!users.containsKey(userId)) {
                throw new NotFoundException(String.format("Этот userId: %d не был найден", userId));
            }

            if (!this.friends.containsKey(userId)) {
                return List.of();
            }

            if (friends.isEmpty()) {
                friends = this.friends.get(userId);

                continue;
            }

            friends.retainAll(this.friends.get(userId));

            if (friends.isEmpty()) {
                return List.of();
            }
        }

        return friends.stream()
                .map(users::get)
                .toList();
    }

    @Override
    @Description("Добавим друга в одностороннем порядке")
    public boolean putFriend(long userId, long friendId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Не нашел userId " + userId);
        }

        if (!users.containsKey(friendId)) {
            throw new NotFoundException("Не нашел friendId " + userId);
        }

        if (!this.friends.containsKey(userId)) {
            var friends = new HashSet<Long>();
            friends.add(friendId);
            this.friends.put(userId, friends);

            log.trace(String.format("userId %d Добавил друга %d", userId, friendId));

            return true;
        }

        var friends = this.friends.get(userId);

        if (friends.contains(friendId)) {
            throw new IdIsAlreadyInUseException(String.format("User[%d] уже содержит друга %d", userId, friendId));
        }

        friends.add(friendId);

        log.trace(String.format("userId %d Добавил друга %d", userId, friendId));

        return true;
    }

    @Override
    @Description("Односторонняя дружба")
    public boolean deleteFriend(long userId, long friendId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Не нашел userId " + userId);
        }

        if (!users.containsKey(friendId)) {
            throw new NotFoundException("Не нашел friendId " + userId);
        }

        if (!this.friends.containsKey(userId)) {
            log.trace(String.format("userId %d не содержит друга %d", userId, friendId));

            return true;
        }

        var friends = this.friends.get(userId);

        if (!friends.contains(friendId)) {
            log.trace(String.format("userId %d не содержит друга %d", userId, friendId));
        } else {
            friends.remove(friendId);

            log.trace(String.format("userId %d удалил друга %d", userId, friendId));
        }

        return true;
    }

    private long getIncrementId() {
        return users.keySet()
                .stream().mapToLong(i -> i)
                .max()
                .orElse(0) + 1;
    }
}
