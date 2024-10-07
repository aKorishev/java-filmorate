package ru.yandex.practicum.filmorate.storage.dbstorage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dbstorage.dto.FilmDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FilmRowMapper implements RowMapper<FilmDto> {
    @Override
    public FilmDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FilmDto.builder()
                .filmId(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releaseDate").toLocalDate())
                .duration(rs.getLong("duration"))
                .mpaId(rs.getLong("mpa_id"))
                .mpaName(rs.getString("mpa_name"))
                .build();
    }

    public Film toData(FilmDto dto) {
        return Film.builder()
                .id(dto.getFilmId())
                .name(dto.getName())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .releaseDate(dto.getReleaseDate())
                .mpa(Mpa.builder()
                        .id(dto.getMpaId())
                        .name(dto.getMpaName())
                        .build())
                .genres(List.of())
                .build();
    }

    public FilmDto toDto(Film film) {
        return FilmDto.builder()
                .filmId(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .duration(film.getDuration())
                .releaseDate(film.getReleaseDate())
                .mpaId(film.getMpa().getId())
                .mpaName(film.getMpa().getName())
                .build();
    }
}
