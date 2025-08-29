package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
@NoArgsConstructor
public class UserController {
    private long generatorId = 0;
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Создаем нового пользователя: {}", user);
        // проверяем выполнение необходимых условий
        validate(user);
        // формируем дополнительные данные
        user.setId(getNextId());
        // добавляем нового пользователя
        users.put(user.getId(), user);
        return user;
    }


    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        log.info("Обновляем пользователя: {}", newUser);
        if (users.containsKey(newUser.getId())) {
            validate(newUser);
            User oldUser = users.get(newUser.getId());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            return oldUser;
        } else {
            throw new IllegalArgumentException("Пользователь с указанным ID не найден");
        }
    }

    public static void validate(User user) {
        validateUser(user);
    }

    private static void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) { // имя может быть пустым -> заполняется логином
            log.info("Пустое имя заполняется логином");
            user.setName(user.getLogin());
        }
    }

    private long getNextId() {
        return ++generatorId;
    }

}
