package ru.yandex.practicum.filmorate.storage.dbstorage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.dbstorage.dto.MpaDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaRowMapper implements RowMapper<MpaDto> {
    @Override
    public MpaDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return MpaDto.builder()
                .mpaId(rs.getLong("mpa_id"))
                .name(rs.getString("name"))
                .build();
    }
}
