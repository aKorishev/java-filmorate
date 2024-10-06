package ru.yandex.practicum.filmorate.storage.dbstorage.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exceptions.NotValidException;
import ru.yandex.practicum.filmorate.storage.dbstorage.mappers.LongRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class DbBaseRepository<T> {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<T> mapper;
    private final LongRowMapper longRowMapper = new LongRowMapper();

    protected Optional<T> findOne(String query, Object... params) {
        try {
            var result = jdbcTemplate.queryForObject(query, mapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    protected List<T> findMany(String query, Object... params) {
        return jdbcTemplate.query(query, mapper, params);
    }

    protected boolean existsQuery(String query, Object... params) {
        query = "select case when exists(" + query + ") then 1 else 0 end return";

        return getLong(query, params) == 1;
    }

    protected boolean delete(String query, long id) {
        int rowsDeleted = jdbcTemplate.update(query, id);
        return rowsDeleted > 0;
    }

    protected boolean update(String query, Object... params) {
        int rowsUpdates = jdbcTemplate.update(query, params);
        return rowsUpdates > 0;
    }

    protected Optional<Long> insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement preparedStatement = conn
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            return preparedStatement;
        }, keyHolder);

        var id = keyHolder.getKeyAs(Integer.class);

        return Optional.ofNullable(id).map(Integer::longValue);
    }

    protected Long getLong(String query, Object... params) {
        var result = jdbcTemplate.queryForObject(query, longRowMapper, params);

        if (result != null)
            return result;

        throw new NotValidException("Запрос некорректный");
    }
}
