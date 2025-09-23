package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
public class UserValidator {

    public static void validate(User user) {
        validateUser(user);
    }

    private static void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) { // имя может быть пустым -> заполняется логином
            log.info("Пустое имя заполняется логином");
            user.setName(user.getLogin());
        }
    }
}
