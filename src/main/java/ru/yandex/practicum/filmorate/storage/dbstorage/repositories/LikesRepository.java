package ru.yandex.practicum.filmorate.storage.dbstorage.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.dbstorage.dto.LikeDto;

import java.util.List;

@Repository
public class LikesRepository extends DbBaseRepository<LikeDto> {
    private static final String FIND_By_PAIR_QUERY = "SELECT * FROM Likes WHERE user_id = ? and film_id = ?";
    private static final String FIND_BY_USERID_QUERY = "SELECT * FROM Likes WHERE user_id = ?";
    private static final String FIND_BY_FILMID_QUERY = "SELECT * FROM Likes WHERE film_id = ?";
    private static final String INSERT_QUERY =
            "INSERT INTO Likes(user_id, film_id)" +
            "VALUES (?, ?) ";
    private static final String DELETE_QUERY = "DELETE Likes WHERE user_id = ? and film_id = ?";
    private static final String DELETE_BY_USERID_QUERY = "DELETE Likes WHERE user_id = ?";
    private static final String DELETE_BY_FILMID_QUERY = "DELETE Likes WHERE film_id = ?";

    public LikesRepository(JdbcTemplate jdbcTemplate, RowMapper<LikeDto> mapper) {
        super(jdbcTemplate, mapper);
    }

    public boolean createLike(long userId, long filmId) {
        return super.update(
                INSERT_QUERY,
                userId,
                filmId);
    }

    public boolean deleteUser(long userId) {
        return super.update(DELETE_BY_USERID_QUERY, userId);
    }

    public boolean deleteFilm(long filmId) {
        return super.update(DELETE_BY_FILMID_QUERY, filmId);
    }

    public boolean delete(long userId, long filmId) {
        return super.update(DELETE_QUERY, userId, filmId);
    }

    public boolean containsUserId(long userId) {
        return super.existsQuery(FIND_BY_USERID_QUERY, userId);
    }

    public boolean containsFilmId(long filmId) {
        return super.existsQuery(FIND_BY_FILMID_QUERY, filmId);
    }

    public boolean containsByPair(long userId, long filmId) {
        return super.existsQuery(FIND_By_PAIR_QUERY, userId, filmId);
    }

    public List<LikeDto> getLikesByUserId(long userId) {
        return super.findMany(FIND_BY_USERID_QUERY, userId);
    }

    public List<LikeDto> getLikesByFilmId(long filmId) {
        return super.findMany(FIND_BY_FILMID_QUERY, filmId);
    }
}
