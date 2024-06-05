package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Storage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserControllerTests {
    @Test
    void getUser() throws Exception {
        var storage = new Storage();
        var controller = new UserController(storage);

        var expectedUser = new User(2L, "email", "2L", "name", LocalDate.now());

        storage.updateUser(new User(1L, "email", "1L", "name", LocalDate.now()));
        storage.updateUser(expectedUser);
        storage.updateUser(new User(3L, "email", "3L", "name", LocalDate.now()));

        var actualUser = controller.getUser(2L);

        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUsers() {
        var storage = new Storage();
        var controller = new UserController(storage);

        storage.updateUser(new User(1L, "1L", "", "name", LocalDate.now()));
        storage.updateUser(new User(2L, "2L", "", "name", LocalDate.now()));
        storage.updateUser(new User(3L, "3L", "", "name", LocalDate.now()));

        Assertions.assertEquals(3, controller.getUsers().size());
    }

    @Test
    void addUser() {
        var storage = new Storage();
        var controller = new UserController(storage);

        storage.updateUser(new User(1L, "1L", "", "name", LocalDate.now()));
        storage.updateUser(new User(2L, "2L", "", "name", LocalDate.now()));
        storage.updateUser(new User(3L, "3L", "", "name", LocalDate.now()));

        controller.updateUser(new User(4L, "4L", "", "name", LocalDate.now()));

        Assertions.assertEquals(4, controller.getUsers().size());
    }

    @Test
    void updateUser() throws Exception {
        var storage = new Storage();
        var controller = new UserController(storage);

        storage.updateUser(new User(1L, "1L", "","name", LocalDate.now()));
        storage.updateUser(new User(2L, "2L", "", "name", LocalDate.now()));
        storage.updateUser(new User(3L, "3L", "", "name", LocalDate.now()));

        controller.updateUser(new User(2L, "10L", "", "test", LocalDate.now()));

        Assertions.assertEquals("test", controller.getUser(2L).getName());
    }
}
