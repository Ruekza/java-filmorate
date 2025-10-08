package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserListExtractor;
import ru.yandex.practicum.filmorate.storage.mappers.UserResultSetExtractor;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserStorage {
    private final JdbcTemplate jdbc;

    @Override
    public User addUser(User user) {
        String sqlQuery = "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (?, ?, ?, ?)";
        Long id = insert(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        user.setId(id);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        String sqlQuery = "DELETE FROM users WHERE user_id = ?";
        if (!userExist(id)) {
            throw new EntityNotFoundException("Пользователь с указанным id не найден");
        }
        jdbc.update(sqlQuery, id);
    }

    @Override
    public User updateUser(User newUser) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        if (!userExist(newUser.getId())) {
            throw new EntityNotFoundException("Пользователь с указанным id не найден");
        }
        jdbc.update(sqlQuery, newUser.getEmail(), newUser.getLogin(), newUser.getName(), newUser.getBirthday(), newUser.getId());
        return newUser;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT u.*, f.user2_id FROM users AS u LEFT JOIN friends AS f "
                + "ON u.user_id = f.user1_id";
        return jdbc.query(sql, new UserListExtractor());
    }

    @Override
    public User getUserById(Long id) {
        String sql = "SELECT u.*, f.user2_id FROM users AS u LEFT JOIN friends AS f" +
                " ON u.user_id = f.user1_id WHERE u.user_id = ?";
        if (!userExist(id)) {
            throw new EntityNotFoundException("Пользователь с указанным id не найден");
        }
        User user = jdbc.query(sql, new UserResultSetExtractor(), new Object[]{id});
        return user;
    }

    public boolean userExist(Long id) {
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        return jdbc.queryForObject(sql, Boolean.class, id);
    }

    protected Long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        // Возвращаем id нового пользователя
        if (id != null) {
            return id;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

}
