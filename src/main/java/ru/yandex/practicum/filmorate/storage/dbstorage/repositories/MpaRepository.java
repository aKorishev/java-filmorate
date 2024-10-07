package ru.yandex.practicum.filmorate.storage.dbstorage.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.NotValidException;
import ru.yandex.practicum.filmorate.storage.dbstorage.dto.MpaDto;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaRepository extends DbBaseRepository<MpaDto> {
    private static final String FIND_ALL_QUERY =
            "SELECT * " +
            "FROM Mpa mpa ";
    private static final String FIND_BY_MPAID_QUERY =
            "SELECT * " +
            "FROM Mpa " +
            "WHERE mpa_id = ? ";
    private static final String INSERT_QUERY =
            "INSERT INTO Mpa(mpa_id, name)" +
            "VALUES (?, ?)";
    private static final String UPDATE_BY_MPAGID_QUERY =
            "UPDATE Mpa SET name = ? " +
            "WHERE mpa_id = ? ";
    private static final String DELETE_BY_MPAID_QUERY =
            "DELETE Mpa " +
            "WHERE mpa_id = ? ";


    public MpaRepository(JdbcTemplate jdbcTemplate, RowMapper<MpaDto> mapper) {
        super(jdbcTemplate, mapper);
    }

    public Optional<Long> createMpa(MpaDto mpaDto) {
        if (!super.existsQuery(FIND_ALL_QUERY)) {
            return super.insert(INSERT_QUERY, 1, mpaDto.getName());
        }

        var lastMpaId = super.getLong("select max(mpa_id) return from Mpa") + 1;

        return super.insert(INSERT_QUERY, lastMpaId + 1, mpaDto.getName());
    }

    public MpaDto deleteMpa(long mpaId) {
        var dto = super.findOne(FIND_BY_MPAID_QUERY, mpaId);

        if (dto.isEmpty())
            throw new NotFoundException("Не нашел рейтинг");

        if (!super.update(DELETE_BY_MPAID_QUERY, mpaId))
            throw new NotValidException("Не смог удалить рейтинг");

        return dto.get();
    }

    public boolean updateMpa(MpaDto mpaDto) {
        return super.update(UPDATE_BY_MPAGID_QUERY, mpaDto.getName(), mpaDto.getMpaId());
    }

    public List<MpaDto> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<MpaDto> findMpaById(long mpaId) {
        return findOne(FIND_BY_MPAID_QUERY, mpaId);
    }

    public boolean containsKey(Long mpaId) {
        return super.existsQuery(FIND_BY_MPAID_QUERY, mpaId);
    }
}
