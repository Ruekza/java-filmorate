package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.JdbcUserRepository;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.util.List;

@Service
public class UserService {

    private final JdbcUserRepository userRepository;
    private final JdbcTemplate jdbc;

    public UserService(JdbcUserRepository userRepository, JdbcTemplate jdbc) {
        this.userRepository = userRepository;
        this.jdbc = jdbc;
    }

    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteUser(id);
    }

    public User updateUser(User newUser) {
        return userRepository.updateUser(newUser);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    public List<Long> addFriend(Long userId, Long anotherUserId) {
        String sql = "INSERT INTO friends(user1_id, user2_id) VALUES (?, ?)";
        if (!userRepository.userExist(userId) || !userRepository.userExist(anotherUserId)) {
            throw new EntityNotFoundException("Пользователь с указанным id не найден");
        } else {
            jdbc.update(sql, userId, anotherUserId);
            // дописать получение списка id друзей
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
