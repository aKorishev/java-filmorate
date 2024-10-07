package ru.yandex.practicum.filmorate.storage.dbstorage.repositories;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.NotValidException;
import ru.yandex.practicum.filmorate.model.SortOrder;
import ru.yandex.practicum.filmorate.storage.SortParameters;
import ru.yandex.practicum.filmorate.storage.dbstorage.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class UsersRepository extends DbBaseRepository<UserDto> {
    private static final String FIND_ALL_QUERY =
            "SELECT * FROM Users ";
    private static final String FIND_BY_ID_QUERY =
            "SELECT * FROM Users WHERE user_id = ? ";
    private static final String UPDATE_QUERY =
            "UPDATE Users SET email = ?, login = ?, name = ?, birthday = ? " +
            "WHERE user_id = ? ";
    private static final String INSERT_QUERY =
            "INSERT INTO Users(email, login, name, birthday) " +
            "VALUES (?, ?, ?, ?) ";
    private static final String DELETE_BY_ID_QUERY =
            "DELETE Users WHERE user_id = ?";
    private static final String FIND_FRIENDS_BY_ID_QUERY =
            "SELECT u.* " +
            "FROM friends f " +
            "   join users u on u.user_id = f.user_id2 " +
            "WHERE f.user_id1 = ? ";

    public UsersRepository(JdbcTemplate jdbcTemplate, RowMapper<UserDto> mapper) {
        super(jdbcTemplate, mapper);
    }

    public boolean containsKey(long userId) {
        return super.existsQuery(FIND_BY_ID_QUERY, userId);
    }

    public UserDto findById(long userId) {
        var userDto = super.findOne(FIND_BY_ID_QUERY, userId);

        if (userDto.isEmpty()) {
            throw new NotFoundException("Отсутсвует запись");
        }

        return userDto.get();
    }

    public List<UserDto> getUsers(@NonNull SortParameters parameters) {
        var query = FIND_ALL_QUERY;

        if (parameters.getSortOrder() == SortOrder.ASCENDING) {
            query += "order by name";
        } else if (parameters.getSortOrder() == SortOrder.DESCENDING) {

            query = "order by name DESC";
        }

        if (parameters.getSize().isPresent()) {
            query += " LIMIT " + parameters.getSize().get();
        }

        if (parameters.getFrom().isPresent()) {
            query += " OFFSET " + parameters.getFrom().get();
        }

        return super.findMany(query);
    }

    public void updateUser(UserDto user) {
        if (!super.update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getUserId())) {
            throw new NotValidException(String.format("Не смог обновить user: %s ", user.getName()));
        }
    }

    public long createUser(UserDto user) {
        var userId = super.insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        if (userId.isEmpty()) {
            throw new NotValidException(String.format("Не смог добавить user: %s ", user.getName()));
        }

        return userId.get();
    }

    public UserDto deleteUser(long userId) {
        var userDto = super.findOne(FIND_BY_ID_QUERY, userId);

        if (userDto.isEmpty()) {
            throw new NotFoundException("Отсутсвует запись для удаления");
        }

        if (!super.delete(DELETE_BY_ID_QUERY, userId)) {
            throw new NotFoundException("Не смог удалить запись");
        }

        return userDto.get();
    }

    public List<UserDto> findFriendsById(Long id) {
        var users = super.findMany(FIND_FRIENDS_BY_ID_QUERY, id);

        log.trace(String.format(
                "Found %d friends %15s for %d user",
                users.size(),
                users.stream().map(UserDto::getUserId).collect(Collectors.toSet()),
                id));

        return users;
    }
}
