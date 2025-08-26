package ru.yandex.practicum.filmorate.controller;

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


    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.info("Добавляем новый фильм: {}", film);
        // проверяем выполнение необходимых условий
        validate(film);
        // формируем дополнительные данные
        film.setId(getNextId());
        // добавляем новый фильм
        films.put(film.getId(), film);
        return film;
    }

    private long getNextId() {
        return ++generatorId;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film newFilm) {
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
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Название фильма пустое");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription() == null || film.getDescription().isBlank()) {
            log.warn("Описание фильма пустое");
            throw new ValidationException("Описание не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Длина описания фильма {} превышает максимальную", film.getDescription().length());
            throw new ValidationException("Максимальная длина описания 200 символов");
        }
        if (film.getReleaseDate() == null) {
            log.warn("Дата релиза фильма пустая");
            throw new ValidationException("Дата релиза не может быть пустой");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата релиза {} ранее 28.12.1895", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть ранее 28.12.1895");
        }
        if (film.getDuration() == null) {
            log.warn("Продолжительность фильма пустая");
            throw new ValidationException("Продолжительность не может быть пустой");
        }
        if (!film.getDuration().isPositive()) {
            log.warn("Продолжительность фильма {} не является положительным числом", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }

}
