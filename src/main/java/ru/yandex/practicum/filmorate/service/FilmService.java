package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public void deleteFilm(Long id) {
        filmStorage.deleteFilm(id);
    }

    public Film updateFilm(Film newFilm) {
        return filmStorage.updateFilm(newFilm);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public Set<Long> addLike(Long userId, Long filmId) {
        if (userStorage.getUserById(userId) == null) {
            throw new EntityNotFoundException("Пользователь с указанным ID не найден");
        }
        Film film = filmStorage.getFilms().get(filmId);
        if (film == null) {
            throw new EntityNotFoundException("Фильм с указанным ID не найден");
        }
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
            film.getLikes().add(userId);
        }
        film.getLikes().add(userId);
        return film.getLikes();
    }

    public void deleteLike(Long userId, Long filmId) {
        if (userStorage.getUserById(userId) == null) {
            throw new EntityNotFoundException("Пользователь с указанным ID не найден");
        }
        Film film = filmStorage.getFilms().get(filmId);
        if (film == null) {
            throw new EntityNotFoundException("Фильм с указанным ID не найден");
        }
        film.getLikes().remove(userId);
    }

    public List<Film> findPopularFilms(int size, int from) {
        Comparator<Film> filmComparator = Comparator.comparing(
                film -> Optional.ofNullable(film.getLikes()).orElse(Collections.emptySet()).size()
        );
        return filmStorage.getFilms().values().stream()
                .sorted(filmComparator.reversed())
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

}
