package ru.yandex.practicum.filmorate.controller;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
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
    public User createUser(@RequestBody User user) {
        log.info("Создаем нового пользователя: {}", user);
        // проверяем выполнение необходимых условий
        validate(user);
        // формируем дополнительные данные
        user.setId(getNextId());
        // добавляем нового пользователя
        users.put(user.getId(), user);
        return user;
    }

    private long getNextId() {
        return ++generatorId;
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
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
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Электронная почта не заполнена");
            throw new ValidationException("Электронная почта не может быть пустой");
        }
        if (!user.getEmail().contains(("@"))) {
            log.warn("Электронная почта {} не содержит символ @", user.getEmail());
            throw new ValidationException("Электронная почта должна содержать @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.info("Логин не заполнен");
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Логин {} содержит пробелы", user.getLogin());
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) { // имя может быть пустым -> заполняется логином
            log.info("Пустое имя заполняется логином");
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null) {
            log.warn("Дата рождения не заполнена");
            throw new ValidationException("Дата рождения не может быть пустой");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения {} в будущем", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть больше текущей даты");
        }
    }

}
