package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }

    public User updateUser(User newUser) {
        return userStorage.updateUser(newUser);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public List<Long> addFriend(Long userId, Long anotherUserId) {
        User user = getUserById(userId);
        User anotherUser = getUserById(anotherUserId);
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
            user.getFriends().add(anotherUserId);
        }
        if (anotherUser.getFriends() == null) {
            anotherUser.setFriends(new HashSet<>());
            anotherUser.getFriends().add(userId);
        }
        user.getFriends().add(anotherUserId);
        anotherUser.getFriends().add(userId);
        return new ArrayList<>(user.getFriends());
    }


    public void deleteFriend(Long userId, Long anotherUserId) {
        User user = getUserById(userId);
        User anotherUser = getUserById(anotherUserId);
        if (user == null || anotherUser == null) {
            throw new EntityNotFoundException("Пользователь с указанным id не найден");
        } else {
            if (user.getFriends() == null || anotherUser.getFriends() == null) {
                return;
            } else {
                user.getFriends().remove(anotherUserId);
                anotherUser.getFriends().remove(userId);
            }
        }
    }

    public List<User> getFriends(Long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new EntityNotFoundException("Пользователь с указанным id не найден");
        }
        Set<Long> friendsId = user.getFriends();
        if (friendsId == null) {
            friendsId = new HashSet<>();
            user.setFriends(friendsId);
        }
        List<User> friends = new ArrayList<>();
        for (Long friendId : friendsId) {
            User friend = userStorage.getUserById(friendId);
            if (friend != null) {
                friends.add(friend);
            }
        }
        return friends;
    }

    public List<User> getCommonFriends(Long userId, Long anotherUserId) {
        User user = getUserById(userId);
        User anotherUser = getUserById(anotherUserId);
        if (user == null || anotherUser == null) {
            throw new EntityNotFoundException("Пользователь с указанным id не найден");
        }
        List<Long> commonFriendsId = new ArrayList<>(user.getFriends());
        commonFriendsId.retainAll(anotherUser.getFriends());
        if (commonFriendsId.isEmpty()) {
            throw new NullPointerException("У вас нет общих друзей");
        }
        // Создаём список общих друзей в виде объектов User
        List<User> commonFriends = new ArrayList<>();
        for (Long friendId : commonFriendsId) {
            User commonFriend = getUserById(friendId);
            if (commonFriend != null) {
                commonFriends.add(commonFriend);
            }
        }
        return commonFriends;
    }

}
