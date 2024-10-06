package ru.yandex.practicum.filmorate.unittests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.storage.SortParameters;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.inmemorystorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.exceptions.IdIsAlreadyInUseException;

import java.time.LocalDate;
import java.util.Set;

public class UserInMemoryStorageTestTest {
    @Test
    void updateUser() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder(1)
                .name("user1")
                .build());

        userStorage.updateUser(initUserBuilder(1)
                .name("user2")
                .build());

        Assertions.assertEquals("user2", userStorage.getUser(1).getName());
    }

    @Test
    void createUser() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder(1)
                .name("user1")
                .build());

        Assertions.assertEquals("user1", userStorage.getUser(1).getName());
    }

    @Test
    void createNotUniqueUser() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder(1)
                .name("user1")
                .build());

        Assertions.assertThrows(
                IdIsAlreadyInUseException.class,
                () -> userStorage.createUser(initUserBuilder(1)
                .name("user2")
                .build()));

        Assertions.assertEquals(1, userStorage.getUsers(SortParameters.builder().build()).size());
    }

    @Test
    void createThreeUsers() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder(1)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder(2)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder(3)
                .name("user3")
                .build());

        Assertions.assertEquals(3, userStorage.getUsers(SortParameters.builder().build()).size());
    }

    @Test
    void containsKeyIsTrue() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder(1)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder(2)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder(3)
                .name("user3")
                .build());

        Assertions.assertTrue(userStorage.containsKey(2));
    }

    @Test
    void containsKeyIsFalse() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder(1)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder(2)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder(3)
                .name("user3")
                .build());

        Assertions.assertFalse(userStorage.containsKey(20));
    }

    @Test
    void getUser() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder(1)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder(2)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder(3)
                .name("user3")
                .build());

        Assertions.assertEquals("user3", userStorage.getUser(3).getName());
    }

    @Test
    void getAllUsersForIds() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder(1)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder(2)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder(3)
                .name("user3")
                .build());

        Assertions.assertEquals(2, userStorage.getUsers(Set.of(1L, 3L)).size());
    }



    @Test
    void getSortedUser() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder(1)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder(2)
                .name("user4")
                .build());

        userStorage.createUser(initUserBuilder(3)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder(4)
                .name("user3")
                .build());

        Assertions.assertEquals(3L, userStorage.getUsers(
                SortParameters.builder()
                        .setAscending()
                        .build())
                .getFirst().getId());
    }

    @Test
    void getSortedDescFilm() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder(1)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder(2)
                .name("user4")
                .build());

        userStorage.createUser(initUserBuilder(3)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder(4)
                .name("user3")
                .build());

        Assertions.assertEquals(2L, userStorage.getUsers(
                SortParameters.builder()
                        .setDescending()
                        .build())
                .getFirst().getId());
    }

    @Test
    void getSkipFilms() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder(1)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder(2)
                .name("user4")
                .build());

        userStorage.createUser(initUserBuilder(3)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder(4)
                .name("user3")
                .build());

        Assertions.assertEquals(2, userStorage.getUsers(
                SortParameters.builder()
                        .from(2)
                        .build()).size());
    }

    @Test
    void getFirst2Films() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder(1)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder(2)
                .name("user4")
                .build());

        userStorage.createUser(initUserBuilder(3)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder(4)
                .name("user3")
                .build());

        Assertions.assertEquals(2, userStorage.getUsers(
                SortParameters.builder()
                        .size(2)
                        .build()).size());
    }

    @Test
    void get2stFilm() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder(1)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder(2)
                .name("user4")
                .build());

        userStorage.createUser(initUserBuilder(3)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder(4)
                .name("user3")
                .build());

        var users = userStorage.getUsers(
                SortParameters.builder()
                        .setDescending()
                        .size(2)
                        .from(1)
                        .build());

        Assertions.assertEquals(2, users.size());

        Assertions.assertEquals(4L, users.getFirst().getId());
    }

    private User.UserBuilder initUserBuilder(int i) {
        return User.builder()
                .id((long) i)
                .email("dfgd@email.ru")
                .login("login")
                .birthday(LocalDate.now());
    }

}
