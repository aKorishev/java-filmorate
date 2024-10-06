package ru.yandex.practicum.filmorate.storage.dbstorage;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.SortParameters;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dbstorage.dto.UserDto;
import ru.yandex.practicum.filmorate.storage.dbstorage.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.storage.dbstorage.repositories.FriendsRepository;
import ru.yandex.practicum.filmorate.storage.dbstorage.repositories.UsersRepository;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@Qualifier("InDbUserStorage")
public class InDbUserStorage implements UserStorage {
    UsersRepository usersRepository;
    UserRowMapper userRowMapper = new UserRowMapper();

    FriendsRepository friendsRepository;

    public InDbUserStorage(UsersRepository usersRepository, FriendsRepository friendsRepository) {
        this.usersRepository = usersRepository;
        this.friendsRepository = friendsRepository;
    }

    @Override
    public boolean containsKey(long userId) {
        return usersRepository.containsKey(userId);
    }

    @Override
    public User getUser(long userId) {
        var dto = usersRepository.findById(userId);

        return userRowMapper.dtoToUser(dto);
    }

    @Override
    public List<User> getUsers(@NonNull SortParameters parameters) {
        return usersRepository.getUsers(parameters)
                .stream()
                .map(userRowMapper::dtoToUser)
                .toList();
    }

    @Override
    public User updateUser(User user) {
        usersRepository.updateUser(userRowMapper.userToDto(user));

        return user;
    }

    @Override
    public User createUser(User user) {
        var userId = usersRepository.createUser(userRowMapper.userToDto(user));

        return user.toBuilder()
                .id(userId)
                .build();
    }

    @Override
    public User deleteUser(long userId) {
        return userRowMapper.dtoToUser(usersRepository.deleteUser(userId));
    }

    @Override
    public List<User> getUsers(Set<Long> ids) {
        List<User> users = new ArrayList<>();

        for (var id : ids) {
            var user = usersRepository.findById(id);
            users.add(userRowMapper.dtoToUser(user));
        }

        return users;
    }

    @Override
    public List<User> getUnionFriends(Set<Long> ids) {
        Map<Long, UserDto> friendMap = new HashMap<>();

        for (var id : ids) {
            List<UserDto> friends = usersRepository.findFriendsById(id);

            for (var friend : friends) {
                var friendId = friend.getUserId();
                if (friendMap.containsKey(friendId)) {
                    continue;
                }

                friendMap.put(friendId, friend);
            }
        }

        return friendMap
                .values()
                .stream()
                .map(userRowMapper::dtoToUser)
                .toList();
    }

    @Override
    public List<User> getIntersectFriends(Set<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }

        var firstId = ids.stream().findFirst().get();

        var result = usersRepository.findFriendsById(firstId);

        ids = ids.stream().skip(1).collect(Collectors.toSet());

        for (var id : ids) {
            if (result.isEmpty()) {
                return List.of();
            }

            var friends = usersRepository.findFriendsById(id);

            if (friends.isEmpty()) {
                return List.of();
            }

            result = friends.stream()
                    .filter(result::contains)
                    .toList();
        }

        return result
                .stream()
                .map(userRowMapper::dtoToUser)
                .toList();
    }

    @Override
    public boolean putFriend(long userId, long friendId) {
        return friendsRepository.createFriend(userId, friendId);
    }

    @Override
    public boolean deleteFriend(long userId, long friendId) {
        return friendsRepository.deleteFriend(userId, friendId);
    }
}
