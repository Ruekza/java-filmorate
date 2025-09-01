package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

public class FilmControllerTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void whenFilmNameIsNullThenNameValidationIsFailed() {
        Film film = new Film(null, null, "desc", LocalDate.of(2020, 05, 11), Duration.ofMinutes(180));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        // Проверяем, что есть нарушение валидации
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Film> violation = violations.iterator().next();
        Assertions.assertEquals("Имя не может быть пустым", violation.getMessage());
    }

    @Test
    void whenFilmNameIsEmptyThenNameValidationIsFailed() {
        Film film = new Film(null, "", "desc", LocalDate.of(2020, 05, 11), Duration.ofMinutes(180));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Film> violation = violations.iterator().next();
        Assertions.assertEquals("Имя не может быть пустым", violation.getMessage());
    }

    @Test
    void whenFilmDescriptionIs201ThenDescriptionValidationIsFailed() {
        Film film = new Film(null, "film", new String(new char[201]).replace('\0', 't'), LocalDate.of(2020, 05, 11), Duration.ofMinutes(180));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Film> violation = violations.iterator().next();
        Assertions.assertEquals("Максимальная длина описания 200 символов", violation.getMessage());
    }

    @Test
    void whenFilmDescriptionIs200ThenDescriptionValidationIsPassed() {
        Film film = new Film(null, "film", new String(new char[200]).replace('\0', 't'), LocalDate.of(2020, 05, 11), Duration.ofMinutes(180));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void whenFilmDescriptionIsNullThenDescriptionValidationIsFailed() {
        Film film = new Film(null, "film", null, LocalDate.of(2020, 05, 11), Duration.ofMinutes(180));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Film> violation = violations.iterator().next();
        Assertions.assertEquals("Описание не может быть пустым", violation.getMessage());
    }

    @Test
    void whenFilmDescriptionIsEmptyThenDescriptionValidationIsFailed() {
        Film film = new Film(null, "film", " ", LocalDate.of(2020, 05, 11), Duration.ofMinutes(180));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Film> violation = violations.iterator().next();
        Assertions.assertEquals("Описание не может быть пустым", violation.getMessage());
    }

    @Test
    void whenReleaseDateIsNullThenDateValidationIsFailed() {
        Film film = new Film(null, "film", "desc", null, Duration.ofMinutes(180));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Film> violation = violations.iterator().next();
        Assertions.assertEquals("Дата релиза не может быть пустой", violation.getMessage());
    }

    @Test
    void whenReleaseDateIsLessMinThrowException() { // дата фильма ранее 28.12.1895
        Film film = new Film(null, "film", "desc", LocalDate.of(1895, 12, 27), Duration.ofMinutes(180));
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film));
    }

    @Test
    void whenReleaseDateIs28Dec1895ThenNoException() {
        Film film = new Film(null, "film", "desc", LocalDate.of(1895, 12, 28), Duration.ofMinutes(180));
        Assertions.assertDoesNotThrow(() -> FilmController.validate(film), "Этот код не должен выбрасывать исключение");
    }

    @Test
    void whenFilmDurationIsNullThenDurationValidationIsFailed() {
        Film film = new Film(null, "film", "desc", LocalDate.of(2000, 12, 28), null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Film> violation = violations.iterator().next();
        Assertions.assertEquals("Продолжительность не может быть пустой", violation.getMessage());
    }

    @Test
    void whenFilmDurationIsNegativeThenThrowException() {
        Film film = new Film(null, "film", "desc", LocalDate.of(2020, 05, 11), Duration.ofMinutes(-180));
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film));
    }

}
