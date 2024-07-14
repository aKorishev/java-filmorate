package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IdIsAlreadyInUseException;
import ru.yandex.practicum.filmorate.model.SortOrder;
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
    public List<User> getUsers(SortOrder sortOrderNames, Optional<Integer> size, Optional<Integer> from) {
        var stream = users.values().stream();

        if (sortOrderNames == SortOrder.ASCENDING) {
            stream = stream.sorted(new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        } else if (sortOrderNames == SortOrder.DESCENDING) {
            stream = stream.sorted(new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return -1 * o1.getName().compareTo(o2.getName());
                }
            });
        }

        if (from.isPresent()) {
            stream = stream.skip(from.get());
        }

        if (size.isPresent()) {
            stream = stream.limit(size.get());
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
