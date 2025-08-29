package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
@NoArgsConstructor
public class FilmController {

    private long generatorId = 0;
    private final Map<Long, Film> films = new HashMap<>();
    public static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Добавляем новый фильм: {}", film);
        // проверяем выполнение необходимых условий
        validate(film);
        // формируем дополнительные данные
        film.setId(getNextId());
        // добавляем новый фильм
        films.put(film.getId(), film);
        return film;
    }


    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        log.info("Обновляем фильм: {}", newFilm);
        if (films.containsKey(newFilm.getId())) {
            validate(newFilm);
            Film oldFilm = films.get(newFilm.getId());
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            return oldFilm;
        } else {
            throw new IllegalArgumentException("Фильм с указанным ID не найден");
        }
    }

    public static void validate(Film film) {
        validateFilm(film);
    }

    private static void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.warn("Дата релиза {} ранее {}", film.getReleaseDate(), MIN_RELEASE_DATE);
            throw new ValidationException("Дата релиза не может быть ранее " + MIN_RELEASE_DATE);
        }
        if (!film.getDuration().isPositive()) {
            log.warn("Продолжительность фильма {} не является положительным числом", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }

    private long getNextId() {
        return ++generatorId;
    }

}
