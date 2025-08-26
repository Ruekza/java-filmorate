package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserControllerTest {

    @Test
    void whenUserEmailIsNullThenThrowException() {
        User user = new User(null, null, "login", "name", LocalDate.of(1980, 05, 11));
        Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user));
    }

    @Test
    void whenUserEmailIsEmptyThenThrowException() {
        User user = new User(null, " ", "login", "name", LocalDate.of(1980, 05, 11));
        Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user));
    }

    @Test
    void whenUserEmailDoesNotContainsDogCharThenThrowException() {
        User user = new User(null, "tttmail.ru", "login", "name", LocalDate.of(1980, 05, 11));
        Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user));
    }

    @Test
    void whenUserLoginIsNullThenThrowException() {
        User user = new User(null, "tom@cat.ru", null, "name", LocalDate.of(1980, 05, 11));
        Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user));
    }

    @Test
    void whenUserLoginIsEmptyThenThrowException() {
        User user = new User(null, "tom@cat.ru", " ", "name", LocalDate.of(1980, 05, 11));
        Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user));
    }

    @Test
    void whenUserLoginContainsBlankCharThenThrowException() {
        User user = new User(null, "tom@cat.ru", "log in", "name", LocalDate.of(1980, 05, 11));
        Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user));
    }

    @Test
    void whenUserNameIsNullThenNoException() {
        User user = new User(null, "tom@cat.ru", "login", null, LocalDate.of(1980, 05, 11));
        Assertions.assertDoesNotThrow(() -> UserController.validate(user), "Этот код не должен выбрасывать исключение");
        UserController userController = new UserController();
        User createdUser = userController.createUser(user);
        Assertions.assertEquals("login", createdUser.getName());
    }

    @Test
    void whenUserBirthdayIsNullThenThrowException() {
        User user = new User(null, "tom@cat.ru", "login", "name", null);
        Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user));
    }

    @Test
    void whenUserBirthdayIsInFutureThenThrowException() {
        User user = new User(null, "tom@cat.ru", "login", "name", LocalDate.now().plusDays(1));
        Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user));
    }

    @Test
    void whenUserBirthdayIsInPresentThenNoException() {
        User user = new User(null, "tom@cat.ru", "login", "name", LocalDate.now());
        Assertions.assertDoesNotThrow(() -> UserController.validate(user), "Этот код не должен выбрасывать исключение");
    }

    @Test
    void whenAddNullUserThenThrowException() {
        User user = new User(null, null, null, null, null);
        UserController userController = new UserController();
        Assertions.assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

}
