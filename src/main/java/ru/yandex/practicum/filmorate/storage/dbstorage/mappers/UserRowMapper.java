package ru.yandex.practicum.filmorate.storage.dbstorage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dbstorage.dto.UserDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<UserDto> {
    public User dtoToUser(UserDto dto) {
        return User.builder()
                .id(dto.getUserId())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .name(dto.getName())
                .birthday(dto.getBirthday())
                .build();
    }

    public UserDto userToDto(User user) {
        return UserDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }

    @Override
    public UserDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserDto.builder()
                .userId(rs.getLong("user_id"))
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
