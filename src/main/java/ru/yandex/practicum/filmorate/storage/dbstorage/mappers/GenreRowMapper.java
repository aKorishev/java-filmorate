package ru.yandex.practicum.filmorate.storage.dbstorage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dbstorage.dto.GenreDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenreRowMapper implements RowMapper<GenreDto> {
    @Override
    public GenreDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return GenreDto
                .builder()
                .genreId(rs.getLong("genre_id"))
                .name(rs.getString("name"))
                .build();
    }

    public GenreDto toDto(Genre genre) {
        return GenreDto
                .builder()
                .genreId(genre.getId())
                .name(genre.getName())
                .build();
    }

    public Genre toData(GenreDto dto) {
        return Genre
                .builder()
                .id(dto.getGenreId())
                .name(dto.getName())
                .build();
    }
}
