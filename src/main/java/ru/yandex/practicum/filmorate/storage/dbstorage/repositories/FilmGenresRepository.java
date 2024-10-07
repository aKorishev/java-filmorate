package ru.yandex.practicum.filmorate.storage.dbstorage.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.dbstorage.mappers.LongRowMapper;

import java.util.List;

@Repository
public class FilmGenresRepository extends DbBaseRepository<Long> {
    private static final String FIND_GENREID_BY_FILMID_QUERY =
            "SELECT genre_id " +
            "FROM Film_Genre f " +
            "WHERE film_id = ? ";
    private static final String INSERT_QUERY =
            "INSERT INTO Film_Genre(film_id, genre_id)" +
            "VALUES (?, ?)";
    private static final String DELETE_BY_FILMID_GENREID_QUERY =
            "DELETE Film_Genre " +
            "WHERE film_id = ? and genre_id = ? ";
    private static final String DELETE_BY_FILMID_QUERY =
            "DELETE Film_Genre " +
             "WHERE film_id = ? ";

    public FilmGenresRepository(JdbcTemplate jdbcTemplate, LongRowMapper mapper) {
        super(jdbcTemplate, mapper);
    }


    public List<Long> findGenresByFilmId(long filmId) {
        return super.findMany(FIND_GENREID_BY_FILMID_QUERY, filmId);
    }

    public boolean insert(long filmId, long genreId) {
        return super.update(INSERT_QUERY, filmId, genreId);
    }

    public boolean delete(long filmId, long genreId) {
        return super.update(DELETE_BY_FILMID_GENREID_QUERY, filmId, genreId);
    }

    public boolean delete(long filmId) {
        return super.update(DELETE_BY_FILMID_QUERY, filmId);
    }

    public boolean containsKey(long filmId) {
        return super.existsQuery(FIND_GENREID_BY_FILMID_QUERY, filmId);
    }
}
