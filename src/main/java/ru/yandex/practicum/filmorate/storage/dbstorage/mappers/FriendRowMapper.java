package ru.yandex.practicum.filmorate.storage.dbstorage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.dbstorage.dto.FriendDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendRowMapper implements RowMapper<FriendDto> {
    @Override
    public FriendDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FriendDto.builder()
                .userId1(rs.getLong("user_id1"))
                .userId2(rs.getLong("user_id2"))
                .build();
    }
}
