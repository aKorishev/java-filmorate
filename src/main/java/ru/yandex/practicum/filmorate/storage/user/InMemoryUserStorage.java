package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
    public void updateUser(User user) {
        users.replace(user.getId(), user);
    }

    @Override
    public void createUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void deleteUser(long userId) {
        users.remove(userId);
    }

    @Override
    public List<User> getUsers(Set<Long> ids) {
        return ids.stream()
                .filter(users::containsKey)
                .map(i -> users.get(i))
                .collect(Collectors.toList());
    }

    @Override
    public long getIncrementId() {
        return users.keySet()
                .stream().mapToLong(i -> i)
                .max()
                .orElse(0) + 1;
    }
}
