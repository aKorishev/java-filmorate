package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.SortOrder;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public class UserInMemoryStorageTestTest {
    @Test
    void updateUser() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder()
                .id(1)
                .name("user1")
                .build());

        userStorage.updateUser(initUserBuilder()
                .id(1)
                .name("user2")
                .build());

        Assertions.assertEquals("user2", userStorage.getUser(1).getName());
    }

    @Test
    void createUser() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder()
                .id(1)
                .name("user1")
                .build());

        Assertions.assertEquals("user1", userStorage.getUser(1).getName());
    }

    @Test
    void createNotUniqueUser() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder()
                .id(1)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(1)
                .name("user2")
                .build());

        Assertions.assertEquals(1, userStorage.getUsers(SortOrder.UNKNOWN, Optional.empty(), Optional.empty()).size());
    }

    @Test
    void createThreeUsers() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder()
                .id(1)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(2)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(3)
                .name("user3")
                .build());

        Assertions.assertEquals(3, userStorage.getUsers(SortOrder.UNKNOWN, Optional.empty(), Optional.empty()).size());
    }

    @Test
    void containsKeyIsTrue() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder()
                .id(1)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(2)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(3)
                .name("user3")
                .build());

        Assertions.assertTrue(userStorage.containsKey(2));
    }

    @Test
    void containsKeyIsFalse() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder()
                .id(1)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(2)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(3)
                .name("user3")
                .build());

        Assertions.assertFalse(userStorage.containsKey(20));
    }

    @Test
    void getUser() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder()
                .id(1)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(2)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(3)
                .name("user3")
                .build());

        Assertions.assertEquals("user3", userStorage.getUser(3).getName());
    }

    @Test
    void getAllUsersForIds() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder()
                .id(1)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(2)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(3)
                .name("user3")
                .build());

        Assertions.assertEquals(2, userStorage.getUsers(Set.of(1L, 3L)).size());
    }



    @Test
    void getSortedUser() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder()
                .id(1)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(2)
                .name("user4")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(3)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(4)
                .name("user3")
                .build());

        Assertions.assertEquals(3L, userStorage.getUsers(SortOrder.ASCENDING, Optional.empty(), Optional.empty()).getFirst().getId());
    }

    @Test
    void getSortedDescFilm() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder()
                .id(1)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(2)
                .name("user4")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(3)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(4)
                .name("user3")
                .build());

        Assertions.assertEquals(2L, userStorage.getUsers(SortOrder.DESCENDING, Optional.empty(), Optional.empty()).getFirst().getId());
    }

    @Test
    void getSkipFilms() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder()
                .id(1)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(2)
                .name("user4")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(3)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(4)
                .name("user3")
                .build());

        Assertions.assertEquals(2, userStorage.getUsers(SortOrder.UNKNOWN, Optional.empty(), Optional.of(2)).size());
    }

    @Test
    void getFirst2Films() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder()
                .id(1)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(2)
                .name("user4")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(3)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(4)
                .name("user3")
                .build());

        Assertions.assertEquals(2, userStorage.getUsers(SortOrder.UNKNOWN, Optional.of(2), Optional.empty()).size());
    }

    @Test
    void get2stFilm() {
        UserStorage userStorage = new InMemoryUserStorage();

        userStorage.createUser(initUserBuilder()
                .id(1)
                .name("user2")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(2)
                .name("user4")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(3)
                .name("user1")
                .build());

        userStorage.createUser(initUserBuilder()
                .id(4)
                .name("user3")
                .build());

        var users = userStorage.getUsers(SortOrder.DESCENDING, Optional.of(2), Optional.of(1));

        Assertions.assertEquals(2, users.size());

        Assertions.assertEquals(4L, users.getFirst().getId());
    }

    private User.UserBuilder initUserBuilder() {
        return User.builder()
                .id(1L)
                .email("dfgd@email.ru")
                .login("login")
                .birthday(LocalDate.now());
    }

}
