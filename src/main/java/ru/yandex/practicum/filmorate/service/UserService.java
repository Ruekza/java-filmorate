package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.JdbcFriendRepository;
import ru.yandex.practicum.filmorate.storage.JdbcUserRepository;

import java.util.List;

@Service
public class UserService {

    private final JdbcUserRepository userRepository;
    private final JdbcFriendRepository friendRepository;

    public UserService(JdbcUserRepository userRepository, JdbcFriendRepository friendRepository) {
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
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
        return friendRepository.addFriend(userId, anotherUserId);
    }

    public void deleteFriend(Long userId, Long anotherUserId) {
        friendRepository.deleteFriend(userId, anotherUserId);
    }

    public List<User> getFriends(Long id) {
        return friendRepository.getFriends(id);
    }

    public List<User> getCommonFriends(Long userId, Long anotherUserId) {
        return friendRepository.getCommonFriends(userId, anotherUserId);
    }

}
