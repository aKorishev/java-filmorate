package ru.yandex.practicum.filmorate.integrationtests;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;

@JdbcTest
@AutoConfigureTestDatabase
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate")
//Как обычно, класс положили, а чтобы он работал не положили
public class FilmoRateApplicationTests {
    //private final InDbUserStorage userStorage;

//    @Test
//    public void testFindUserById() {
//
//        User user = userStorage.getUser(1);
//
//        assertThat(user).hasFieldOrPropertyWithValue("id", 1);
//    }
}