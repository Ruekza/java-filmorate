package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    public Map<Long, User> getUsers();

    public User addUser(User user);

    public void deleteUser(Long id);

    public User updateUser(User newUser);

    public List<User> getAllUsers();

    public User getUserById(Long id);
}
