package ru.yandex.practicum.filmorate.storage.user;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.model.SortParameters;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    User updateUser(User user);

    User createUser(User user);

    User deleteUser(long userId);

    boolean containsKey(long userId);

    User getUser(long userId);

    public List<User> getUsers(@NonNull SortParameters parameters);

    public List<User> getUsers(Set<Long> ids);
}
