package ru.yandex.practicum.filmorate.storage;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    User updateUser(User user);

    User createUser(User user);

    User deleteUser(long userId);

    boolean containsKey(long userId);

    User getUser(long userId);

    List<User> getUsers(@NonNull SortParameters parameters);

    List<User> getUsers(Set<Long> ids);

    List<User> getUnionFriends(Set<Long> ids);

    List<User> getIntersectFriends(Set<Long> ids);

    boolean putFriend(long userId, long friendId);

    boolean deleteFriend(long userId, long friendId);
}
