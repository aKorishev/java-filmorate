package ru.yandex.practicum.filmorate.storage.user;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IdIsAlreadyInUseException;
import ru.yandex.practicum.filmorate.model.SortOrder;
import ru.yandex.practicum.filmorate.model.SortParameters;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    Map<Long, User> users = new HashMap<>();

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

    private long getIncrementId() {
        return users.keySet()
                .stream().mapToLong(i -> i)
                .max()
                .orElse(0) + 1;
    }
}
