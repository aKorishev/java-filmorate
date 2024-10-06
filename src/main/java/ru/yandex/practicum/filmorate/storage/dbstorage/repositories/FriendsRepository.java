package ru.yandex.practicum.filmorate.storage.dbstorage.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dbstorage.dto.UserDto;
import ru.yandex.practicum.filmorate.storage.dbstorage.mappers.UserRowMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Repository
public class FriendsRepository extends DbBaseRepository<UserDto> {
    private static final String FIND_BY_USERID_QUERY =
            "SELECT u.* " +
            "FROM Friends f " +
            "   JOIN USERS u on u.user_id = f.user_id2" +
            "WHERE user_id1 = ?";
    private static final String INSERT_QUERY =
            "INSERT INTO Friends(user_id1, user_id2)" +
            "VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE Friends WHERE user_id1 = ? and user_id2 = ?";
    private static final String DELETE_BY_USER_ID1_QUERY = "DELETE Friends WHERE user_id1 = ?";
    private static final String DELETE_BY_USER_ID2_QUERY = "DELETE Friends WHERE user_id2 = ?";

    private final UserRowMapper mapper = new UserRowMapper();

    public FriendsRepository(JdbcTemplate jdbcTemplate, RowMapper<UserDto> mapper) {
        super(jdbcTemplate, mapper);
    }

    public boolean createFriend(long userId1, long userId2) {
        return super.update(INSERT_QUERY, userId1, userId2);
    }

    public boolean deleteFriend(long userId1, long userId2) {
        return super.update(DELETE_QUERY, userId1, userId2);
    }

    public void deleteFriendsByUSer1(long userId) {
        super.update(DELETE_BY_USER_ID1_QUERY, userId);
    }

    public void deleteFriendsByUSer2(long userId) {
        super.update(DELETE_BY_USER_ID2_QUERY, userId);
    }

    public List<User> getFriends(List<User> users) {
        var result = new ArrayList<User>();
        var found = new HashSet<Long>();
        var checked = new HashSet<Long>();

        for (var user : users) {
            var id = user.getId();
            if (checked.contains(id))
                continue;

            for (var friend : super.findMany(FIND_BY_USERID_QUERY, id)) {
                var friendId = friend.getUserId();
                if (found.contains(friendId))
                    continue;

                result.add(mapper.dtoToUser(friend));

                found.add(friendId);
            }

            checked.add(id);
        }

        return result;
    }
}
