package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserCreate;

import java.time.LocalDate;
import java.util.Set;

public class UserControllerTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void whenUserEmailIsNullThenEmailValidationIsFailed() {
        User user = new User(null, null, "login", "name", LocalDate.of(1980, 05, 11));
        Set<ConstraintViolation<User>> violations = validator.validate(user, UserCreate.class);
        // Проверяем, что есть нарушение валидации
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        Assertions.assertEquals("Почта не может быть пустой", violation.getMessage());
    }

    @Test
    void whenUserEmailIsEmptyThenEmailValidationIsFailed() {
        User user = new User(null, " ", "login", "name", LocalDate.of(1980, 05, 11));
        Set<ConstraintViolation<User>> violations = validator.validate(user, UserCreate.class);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        for (ConstraintViolation<User> violation : violations) {
            String message = violation.getMessage();
            Assertions.assertTrue(message.equals("Почта не может быть пустой"));
        }
    }

    @Test
    void whenUserEmailDoesNotContainsDogCharThenEmailValidationIsFailed() {
        User user = new User(null, "tttmail.ru", "login", "name", LocalDate.of(1980, 05, 11));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        Assertions.assertEquals("Почта должна быть корректной", violation.getMessage());
    }

    @Test
    void whenUserLoginIsNullThenLoginValidationIsFailed() {
        User user = new User(null, "tom@cat.ru", null, "name", LocalDate.of(1980, 05, 11));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        Assertions.assertEquals("Логин не может быть пустым", violation.getMessage());
    }

    @Test
    void whenUserLoginIsEmptyThenLoginValidationIsFailed() {
        User user = new User(null, "tom@cat.ru", "", "name", LocalDate.of(1980, 05, 11));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(2, violations.size());
        for (ConstraintViolation<User> violation : violations) {
            String message = violation.getMessage();
            Assertions.assertTrue(message.equals("Логин не может быть пустым") || message.equals("Логин не может содержать пробелы"));
        }
    }

    @Test
    void whenUserLoginContainsBlankCharThenLoginValidationIsFailed() {
        User user = new User(null, "tom@cat.ru", "log in", "name", LocalDate.of(1980, 05, 11));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        Assertions.assertEquals("Логин не может содержать пробелы", violation.getMessage());
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
    void whenUserBirthdayIsNullThenBirthdayValidationIsFailed() {
        User user = new User(null, "tom@cat.ru", "login", "name", null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        Assertions.assertEquals("Дата рождения не может быть пустой", violation.getMessage());
    }

    @Test
    void whenUserBirthdayIsInFutureThenBirthdayValidationIsFailed() {
        User user = new User(null, "tom@cat.ru", "login", "name", LocalDate.now().plusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        Assertions.assertEquals("Дата рождения не может быть в будущем", violation.getMessage());
    }

    @Test
    void whenUserBirthdayIsInPresentThenBirthdayValidationIsPassed() {
        User user = new User(null, "tom@cat.ru", "login", "name", LocalDate.now());
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertTrue(violations.isEmpty());
    }

}
