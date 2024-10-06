package ru.yandex.practicum.filmorate.storage.dbstorage.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.NotValidException;
import ru.yandex.practicum.filmorate.storage.dbstorage.dto.GenreDto;

import java.util.List;
import java.util.Optional;

@Repository
public class GenresRepository extends DbBaseRepository<GenreDto> {
    private static final String FIND_ALL_QUERY =
            "SELECT * " +
            "FROM Genres f ";
    private static final String FIND_BY_GENREID_QUERY =
            "SELECT * " +
            "FROM Genres f " +
            "WHERE genre_id = ?";
    private static final String INSERT_QUERY =
            "INSERT INTO Genres(name)" +
            "VALUES (?)";
    private static final String UPDATE_BY_GENREID_QUERY =
            "UPDATE Genres SET name = ? " +
            "WHERE genre_id = ? ";
    private static final String DELETE_BY_GENREID_QUERY =
            "DELETE Genres " +
            "WHERE genre_id = ? ";
    private static final String FIND_BY_FILMID =
            "SELECT g.* " +
            "FROM Film_Genre fg " +
            "    JOIN Genres g on g.genre_id = fg.genre_id " +
            "WHERE film_id = ? ";

    public GenresRepository(JdbcTemplate jdbcTemplate, RowMapper<GenreDto> mapper) {
        super(jdbcTemplate, mapper);
    }

    public Optional<Long> createGenre(GenreDto genreDto) {
        return super.insert(INSERT_QUERY, genreDto.getName());
    }

    public GenreDto deleteGenre(long genreId) {
        var genre = super.findOne(FIND_BY_GENREID_QUERY, genreId);

        if (genre.isEmpty())
            throw new NotFoundException("Не нашел жанр");

        if (!super.update(DELETE_BY_GENREID_QUERY, genreId))
            throw new NotValidException("Не смог удалить жанр");

        return genre.get();
    }

    public boolean updateGenre(GenreDto genreDto) {
        return super.update(UPDATE_BY_GENREID_QUERY, genreDto.getName(), genreDto.getGenreId());
    }

    public List<GenreDto> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<GenreDto> findGenreById(long genreId) {
        return findOne(FIND_BY_GENREID_QUERY, genreId);
    }

    public List<GenreDto> findByFilmID(long filmId) {
        return findMany(FIND_BY_FILMID, filmId);
    }

    public boolean containsKey(Long id) {
        return super.existsQuery(FIND_BY_GENREID_QUERY, id);
    }
}
