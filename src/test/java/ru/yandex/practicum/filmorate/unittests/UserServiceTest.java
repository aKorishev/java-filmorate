package ru.yandex.practicum.filmorate.unittests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.IdIsAlreadyInUseException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.SortOrder;
import ru.yandex.practicum.filmorate.storage.SortParameters;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.inmemorystorage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

public class UserServiceTest {
    @Test
    void getUser() {
        var userService = new UserService(
                new InMemoryUserStorage());

        userService.postUser(initUserBuilder(1).build());

        Assertions.assertNotNull(userService.getUser(1L));
    }

    @Test
    void getThrowOnGetUser() {
        var userService = new UserService(
                new InMemoryUserStorage());

        userService.postUser(
                initUserBuilder(1)
                        .build());

        Assertions.assertThrows(NotFoundException.class, () -> userService.getUser(3L));
    }

    @Test
    void get2stUser() {
        var userService = new UserService(
                new InMemoryUserStorage());

        userService.postUser(
                initUserBuilder(1)
                        .name("user2")
                        .build());

        userService.postUser(
                initUserBuilder(2)
                        .name("user")
                        .build());

        userService.postUser(
                initUserBuilder(3)
                        .name("user4")
                        .build());

        userService.postUser(
                initUserBuilder(4)
                        .name("user3")
                        .build());

        var users = userService.getUsers(
                SortParameters.builder()
                        .sortOrder(SortOrder.DESCENDING)
                        .size(2)
                        .from(1)
                        .build());

        Assertions.assertEquals(2, users.size());

        Assertions.assertEquals(4L, users.getFirst().getId());
    }

    @Test
    void deleteUser() {
        var userService = new UserService(
                new InMemoryUserStorage());

        userService.postUser(
                initUserBuilder(1)
                        .name("user1")
                        .build());

        userService.postUser(
                initUserBuilder(2)
                        .name("user2")
                        .build());

        userService.postUser(
                initUserBuilder(3)
                        .name("user3")
                        .build());

        userService.deleteUser(2);

        Assertions.assertEquals(2, userService.getUsers(SortParameters.builder().build()).size());
    }

    @Test
    void getThrowOnDeleteUser() {
        var userService = new UserService(
                new InMemoryUserStorage());

        userService.postUser(
                initUserBuilder(1)
                        .name("user1")
                        .build());

        userService.postUser(
                initUserBuilder(2)
                        .name("user2")
                        .build());

        userService.postUser(
                initUserBuilder(3)
                        .name("user3")
                        .build());

        Assertions.assertThrows(NotFoundException.class, () -> userService.getUser(5L));
    }

    @Test
    void updateNewUser() {
        var userService = new UserService(
                new InMemoryUserStorage());

        userService.postUser(
                initUserBuilder(1)
                        .name("user1")
                        .build());

        userService.postUser(
                initUserBuilder(2)
                        .name("user2")
                        .build());

        Assertions.assertEquals(2, userService.getUsers(SortParameters.builder().build()).size());
    }

    @Test
    void updateOldUser() {
        var userService = new UserService(
                new InMemoryUserStorage());

        userService.postUser(
                initUserBuilder(1)
                        .name("user1")
                        .build());

        userService.postUser(
                initUserBuilder(1)
                        .name("user2")
                        .build());

        Assertions.assertEquals(1, userService.getUsers(SortParameters.builder().build()).size());

        Assertions.assertEquals("user2", userService.getUser(1).getName());
    }

    @Test
    void putFriends() {
        var userService = new UserService(
                new InMemoryUserStorage());

        for (int i = 1; i < 20; i++) {
            userService.postUser(initUserBuilder(i)
                    .name("user" + i)
                    .build());
        }

        userService.putFriend(1,2);

        Assertions.assertTrue(userService.getFriends(1).stream().anyMatch(i -> i.getId() == 2L));
    }

    @Test
    void getNotFoundUserExceptionputFriend() {
        var userService = new UserService(
                new InMemoryUserStorage());

        userService.postUser(
                initUserBuilder(1)
                        .name("user1")
                        .build());

        userService.postUser(
                initUserBuilder(2)
                        .name("user1")
                        .build());

        Assertions.assertThrows(NotFoundException.class, () -> userService.putFriend(3, 2));
    }

    @Test
    void getNotFoundFriendExceptionputFriend() {
        var userService = new UserService(
                new InMemoryUserStorage());

        userService.postUser(
                initUserBuilder(1)
                        .name("user1")
                        .build());

        userService.postUser(
                initUserBuilder(2)
                        .name("user1")
                        .build());

        Assertions.assertThrows(NotFoundException.class, () -> userService.putFriend(1, 3L));
    }

    @Test
    void getNotFoundUserExceptionPutFriendSelf() {
        var userService = new UserService(
                new InMemoryUserStorage());

        userService.postUser(
                initUserBuilder(1)
                        .name("user1")
                        .build());

        Assertions.assertThrows(IdIsAlreadyInUseException.class, () -> userService.putFriend(1, 1));
    }

    @Test
    void getUnionFriends() {
        var userService = new UserService(
                new InMemoryUserStorage());

        for (int i = 1; i < 20; i++) {
            userService.postUser(initUserBuilder(i)
                    .name("user" + i)
                    .build());
        }

        userService.putFriend(1,3);
        userService.putFriend(1, 5);
        userService.putFriend(1, 7);

        userService.putFriend(2, 3);
        userService.putFriend(2, 9);

        Assertions.assertEquals(4, userService.getUnionFriends(List.of(1L, 2L)).size());
    }

    @Test
    void getIntersectFriends() {
        var userService = new UserService(
                new InMemoryUserStorage());

        for (int i = 1; i < 20; i++) {
            userService.postUser(initUserBuilder(i)
                    .name("user" + i)
                    .build());
        }

        userService.putFriend(1,3);
        userService.putFriend(1, 5);
        userService.putFriend(1, 7);

        userService.putFriend(2, 3);
        userService.putFriend(2, 9);

        Assertions.assertEquals(1, userService.getIntersectFriends(List.of(1L, 2L)).size());
    }

    @Test
    void getNotFoundUserExceptionDeleteFriend() {
        var userService = new UserService(
                new InMemoryUserStorage());

        userService.postUser(
                initUserBuilder(1)
                        .name("user1")
                        .build());

        userService.postUser(
                initUserBuilder(2)
                        .name("user1")
                        .build());

        userService.putFriend(1,2);

        Assertions.assertThrows(NotFoundException.class, () -> userService.putFriend(3, 2L));
    }

    @Test
    void getNotFoundFriendExceptionDeleteFriend() {
        var userService = new UserService(
                new InMemoryUserStorage());

        userService.postUser(
                initUserBuilder(1)
                        .name("user1")
                        .build());

        userService.postUser(
                initUserBuilder(2)
                        .name("user1")
                        .build());

        userService.putFriend(1,2);

        Assertions.assertThrows(NotFoundException.class, () -> userService.putFriend(1, 3L));
    }


    private User.UserBuilder initUserBuilder(int id) {
        return User.builder()
                .id((long) id)
                .email("dfgd@email.ru")
                .login("login")
                .birthday(LocalDate.now());
    }

}
