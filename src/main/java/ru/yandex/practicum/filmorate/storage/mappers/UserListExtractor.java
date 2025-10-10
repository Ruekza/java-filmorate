package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserListExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<User> users = new ArrayList<>();
        Long currentUserId = null;
        User currentUser = null;

        while (rs.next()) {
            Long userId = rs.getLong("user_id");
            if (currentUserId == null || !currentUserId.equals(userId)) {
                if (currentUser != null) {
                    users.add(currentUser);
                }
                currentUserId = userId;
                currentUser = new User();
                currentUser.setId(rs.getLong("user_id"));
                currentUser.setEmail(rs.getString("email"));
                currentUser.setLogin(rs.getString("login"));
                currentUser.setName(rs.getString("name"));
                currentUser.setBirthday(rs.getDate("birthday").toLocalDate());
            }
        }
        if (currentUser != null) {
            users.add(currentUser);
        }
        return users;
    }

}
