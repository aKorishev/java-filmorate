package ru.yandex.practicum.filmorate.integrationtests;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.storage.dbstorage.InDbUserStorage;
import ru.yandex.practicum.filmorate.storage.dbstorage.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.storage.dbstorage.repositories.UsersRepository;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate")
@Import({UsersRepository.class, UserRowMapper.class})
//Как обычно, класс положили, а чтобы он работал не положили
public class FilmorateApplicationTests {
    private final InDbUserStorage userStorage;

//    @Test
//    public void testFindUserById() {
//
//        User user = userStorage.getUser(1);
//
//        assertThat(user).hasFieldOrPropertyWithValue("id", 1);
//    }
}