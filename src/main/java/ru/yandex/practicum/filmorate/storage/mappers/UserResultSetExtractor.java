package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class UserResultSetExtractor implements ResultSetExtractor<User> {
    @Override
    public User extractData(ResultSet rs) throws SQLException, DataAccessException {
        User user = null;
        while (rs.next()) {
            if (user == null) {
                user = new User();
                user.setId(rs.getLong("user_id"));
                user.setEmail(rs.getString("email"));
                user.setLogin(rs.getString("login"));
                user.setName(rs.getString("name"));
                user.setBirthday(rs.getDate("birthday").toLocalDate());
            }
            // Обработка друзей
            if (user.getFriends() == null) {
                user.setFriends(new HashSet<>());
            }
            user.getFriends().add(rs.getLong("user2_id"));
        }
        return user;
    }

}
