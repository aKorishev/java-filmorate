package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.SortOrder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {
    void updateUser(User user);

    void createUser(User user);

    void deleteUser(long userId);

    boolean containsKey(long userId);

    User getUser(long userId);

    public List<User> getUsers(SortOrder sortOrderNames, Optional<Integer> size, Optional<Integer> from);

    public List<User> getUsers(Set<Long> ids);

    long getIncrementId();
}
