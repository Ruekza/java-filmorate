package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Getter
@Component
public class InMemoryUserStorage implements UserStorage {

    private long generatorId = 0;
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    @Override
    public User addUser(User user) {
        if (emails.contains(user.getEmail())) {
            throw new ValidationException("Электронная почта " + user.getEmail() + " уже зарегистрирована");
        } else {
            user.setId(getNextId());
            users.put(user.getId(), user);
            emails.add(user.getEmail());
            return user;
        }
    }

    @Override
    public void deleteUser(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new EntityNotFoundException("Пользователь с указанным id не найден");
        }
        emails.remove(user.getEmail());
        users.remove(id);
    }

    @Override
    public User updateUser(User newUser) {
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            String email = oldUser.getEmail();
            if(emails.contains(newUser.getEmail())) {
                throw new ValidationException("Электронная почта " + newUser.getEmail() + " уже зарегистрирована");
            } else {
                oldUser.setEmail(newUser.getEmail());
            }
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            oldUser.setFriends(newUser.getFriends());
            emails.remove(email);
            emails.add(newUser.getEmail());
            return oldUser;
        } else {
            throw new EntityNotFoundException("Пользователь с указанным ID не найден");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new EntityNotFoundException("Пользователь с указанным id не найден");
        }
        return user;
    }

    private long getNextId() {
        return ++generatorId;
    }

}
