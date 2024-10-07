package ru.yandex.practicum.filmorate.integrationtests;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dbstorage.InDbUserStorage;
import ru.yandex.practicum.filmorate.storage.dbstorage.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.storage.dbstorage.repositories.FriendsRepository;
import ru.yandex.practicum.filmorate.storage.dbstorage.repositories.UsersRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate")
@Import({UsersRepository.class, UserRowMapper.class})
public class FilmorateApplicationTests {
    private static UserStorage userStorage;

    @BeforeAll
    public static void init() {
        var config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;INIT=runscript from 'classpath:/schema.sql'");
        config.setUsername("database_username");
        config.setPassword("database_password");

        var dataSource = new HikariDataSource(config);
        var jdbcTemplate = new JdbcTemplate(dataSource);

        var userRowMapper = new UserRowMapper();

        userStorage = new InDbUserStorage(
                new UsersRepository(jdbcTemplate, userRowMapper),
                new FriendsRepository(jdbcTemplate, userRowMapper));
    }

    @Test
    public void testFindUserById() {
        userStorage.createUser(
                User.builder()
                        .email("dfgd@email.ru")
                        .login("login")
                        .birthday(LocalDate.now())
                        .build());

        User user = userStorage.getUser(1);

        assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
    }
}