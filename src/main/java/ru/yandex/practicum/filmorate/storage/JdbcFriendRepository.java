package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcFriendRepository {
    private final JdbcTemplate jdbc;
    private final JdbcUserRepository userRepository;

    public List<Long> addFriend(Long userId, Long anotherUserId) {
        String sql = "INSERT INTO friends(user1_id, user2_id) VALUES (?, ?)";
        if (!userRepository.userExist(userId) || !userRepository.userExist(anotherUserId)) {
            throw new EntityNotFoundException("Пользователь с указанным id не найден");
        } else {
            jdbc.update(sql, userId, anotherUserId);
            return getFriendsId(userId);
        }
    }

    public void deleteFriend(Long userId, Long anotherUserId) {
        String sql = "DELETE FROM friends WHERE user1_id = ? AND user2_id = ?";
        if (!userRepository.userExist(userId) || !userRepository.userExist(anotherUserId)) {
            throw new EntityNotFoundException("Пользователь с указанным id не найден");
        } else {
            jdbc.update(sql, userId, anotherUserId);
        }
    }

    public List<User> getFriends(Long id) {
        String sql = "SELECT * FROM users WHERE user_id IN(SELECT user2_id FROM friends WHERE user1_id = ?)";
        if (!userRepository.userExist(id)) {
            throw new EntityNotFoundException("Пользователь с указанным id не найден");
        } else {
            return jdbc.query(sql, new UserRowMapper(), id);
        }
    }

    public List<User> getCommonFriends(Long userId, Long anotherUserId) {
        String sql = "SELECT * FROM users " +
                "WHERE user_id IN (SELECT user2_id FROM friends WHERE user1_id = ? " +
                "AND user2_id IN(SELECT user2_id FROM friends WHERE user1_id = ?))";
        if (!userRepository.userExist(userId) || !userRepository.userExist(anotherUserId)) {
            throw new EntityNotFoundException("Пользователь с указанным id не найден");
        } else {
            return jdbc.query(sql, new UserRowMapper(), userId, anotherUserId);
        }
    }

    // Метод для получения списка id друзей
    public List<Long> getFriendsId(Long userId) {
        String sql = "SELECT user2_id FROM friends WHERE user1_id = ?";
        return jdbc.queryForList(sql, Long.class, userId);
    }

}
