package ru.yandex.practicum.filmorate.storage.dbstorage.repositories;

import lombok.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.NotValidException;
import ru.yandex.practicum.filmorate.model.SortOrder;
import ru.yandex.practicum.filmorate.storage.SortParameters;
import ru.yandex.practicum.filmorate.storage.dbstorage.dto.FilmDto;

import java.util.List;

@Repository
public class FilmsRepository extends DbBaseRepository<FilmDto> {
    private static final String FIND_ALL_QUERY =
            "SELECT f.*, m.name mpa_name " +
            "FROM Films f " +
            "    left join MPA m on m.mpa_id = f.mpa_id ";
    private static final String FIND_BY_ID_QUERY =
            "SELECT f.*, m.name mpa_name " +
            "FROM Films f " +
            "    left join MPA m on m.mpa_id = f.mpa_id " +
            "WHERE film_id = ? ";
    private static final String UPDATE_QUERY =
            "UPDATE Films SET name = ?, description = ?, releaseDate = ?, duration = ?, mpa_id = ? " +
            "WHERE film_id = ? ";
    private static final String INSERT_QUERY =
            "INSERT INTO Films(name, description, releaseDate, duration, mpa_id) " +
            "VALUES (?, ?, ?, ?, ?) ";
    private static final String DELETE_BY_ID_QUERY = "DELETE Films WHERE film_id = ? ";

    public FilmsRepository(JdbcTemplate jdbcTemplate, RowMapper<FilmDto> mapper) {
        super(jdbcTemplate, mapper);
    }


    public boolean containsKey(long filmId) {
        return super.existsQuery(FIND_BY_ID_QUERY, filmId);
    }

    public FilmDto findById(long filmId) {
        var filmDTO = super.findOne(FIND_BY_ID_QUERY, filmId);

        if (filmDTO.isEmpty()) {
            throw new NotFoundException("Отсутсвует запись");
        }

        return filmDTO.get();
    }

    public List<FilmDto> getFilms(@NonNull SortParameters parameters) {
        var query = FIND_ALL_QUERY;

        if (parameters.getSortOrder() == SortOrder.ASCENDING) {
            query += "   left join( select film_id, count(*) cnt" +
                     "              from likes " +
                     "              group by film_id) l on l.film_id = f.film_id " +
                     "order by case when l.cnt is null then 0 else l.cnt end";
        } else if (parameters.getSortOrder() == SortOrder.DESCENDING) {

            query += "   left join( select film_id, count(*) cnt" +
                     "              from likes " +
                     "              group by film_id) l on l.film_id = f.film_id " +
                     "order by case when l.cnt is null then 0 else l.cnt end DESC";
        }

        if (parameters.getSize().isPresent()) {
            query += " LIMIT " + parameters.getSize().get();
        }

        if (parameters.getFrom().isPresent()) {
            query += " OFFSET " + parameters.getFrom().get();
        }

        return super.findMany(query);
    }

    public void updateFilm(FilmDto film) {
        if (!super.update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaId(),
                film.getFilmId())) {
            throw new NotValidException(String.format("Не смог обновить film: %s ", film.getName()));
        }
    }

    public long createFilm(FilmDto film) {
        var filmId = super.insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaId());

        if (filmId.isEmpty()) {
            throw new NotValidException(String.format("Не смог добавить film: %s ", film.getName()));
        }

        return filmId.get();
    }

    public FilmDto deleteFilm(long filmId) {
        var filmDTO = super.findOne(FIND_BY_ID_QUERY, filmId);

        if (filmDTO.isEmpty()) {
            throw new NotFoundException("Отсутсвует запись для удаления");
        }

        if (!super.delete(DELETE_BY_ID_QUERY, filmId)) {
            throw new NotFoundException("Не смог удалить запись");
        }

        return filmDTO.get();
    }
}
