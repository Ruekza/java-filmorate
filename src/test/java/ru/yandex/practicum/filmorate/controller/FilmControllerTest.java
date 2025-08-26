package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

public class FilmControllerTest {

    @Test
    void whenFilmNameIsNullThenThrowException() {
        Film film = new Film(null, null, "desc", LocalDate.of(2020, 05, 11), Duration.ofMinutes(180));
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film));
    }

    @Test
    void whenFilmNameIsEmptyThenThrowException() {
        Film film = new Film(null, "", "desc", LocalDate.of(2020, 05, 11), Duration.ofMinutes(180));
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film));
    }

    @Test
    void whenFilmDescriptionIs201ThenThrowException() {
        Film film = new Film(null, "film", new String(new char[201]).replace('\0', 't'), LocalDate.of(2020, 05, 11), Duration.ofMinutes(180));
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film));
    }

    @Test
    void whenFilmDescriptionIs200ThenNoException() {
        Film film = new Film(null, "film", new String(new char[200]).replace('\0', 't'), LocalDate.of(2020, 05, 11), Duration.ofMinutes(180));
        Assertions.assertDoesNotThrow(() -> FilmController.validate(film), "Этот код не должен выбрасывать исключение");
    }

    @Test
    void whenFilmDescriptionIsNullThenThrowException() {
        Film film = new Film(null, "film", null, LocalDate.of(2020, 05, 11), Duration.ofMinutes(180));
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film));
    }

    @Test
    void whenFilmDescriptionIsEmptyThenThrowException() {
        Film film = new Film(null, "film", " ", LocalDate.of(2020, 05, 11), Duration.ofMinutes(180));
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film));
    }

    @Test
    void whenReleaseDateIsNullThenThrowException() {
        Film film = new Film(null, "film", "desc", null, Duration.ofMinutes(180));
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film));
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
    void whenFilmDurationIsNullThenThrowException() {
        Film film = new Film(null, "film", "desc", LocalDate.of(2000, 12, 28), null);
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film));
    }

    @Test
    void whenFilmDurationIsNegativeThenThrowException() {
        Film film = new Film(null, "film", "desc", LocalDate.of(2020, 05, 11), Duration.ofMinutes(-180));
        Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film));
    }

    @Test
    void whenAddNullFilmThenThrowException() {
        Film film = new Film(null, null, null, null, null);
        FilmController filmController = new FilmController();
        Assertions.assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

}
