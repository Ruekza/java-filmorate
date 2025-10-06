package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.JdbcFilmRepository;
import ru.yandex.practicum.filmorate.storage.JdbcUserRepository;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.MpaRowMapper;

import java.util.*;


@Service
public class FilmService {
    private final JdbcFilmRepository filmRepository;
    private final JdbcUserRepository userRepository;
    private final JdbcTemplate jdbc;

    @Autowired
    public FilmService(JdbcFilmRepository filmRepository, JdbcUserRepository userRepository, JdbcTemplate jdbc) {
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
        this.jdbc = jdbc;
    }

    public Film addFilm(Film film) {
        return filmRepository.addFilm(film);
    }

    public void deleteFilm(Long id) {
        filmRepository.deleteFilm(id);
    }

    public Film updateFilm(Film newFilm) {
        return filmRepository.updateFilm(newFilm);
    }

    public List<Film> getAllFilms() {
        return filmRepository.getAllFilms();
    }

    public Film getFilmById(Long id) {
        return filmRepository.getFilmById(id);
    }

    public void addLike(Long userId, Long filmId) { // убрали Set<Long>
        if (!userRepository.userExist(userId)) {
            throw new EntityNotFoundException("Пользователь с указанным ID не найден");
        }
        if (!filmRepository.filmExist(filmId)) {
            throw new EntityNotFoundException("Фильм с указанным ID не найден");
        }
        String sql = "INSERT INTO likes(user_id, film_id) VALUES (?, ?)";
        jdbc.update(sql, userId, filmId);
    }

    public void deleteLike(Long userId, Long filmId) {
        if (!userRepository.userExist(userId)) {
            throw new EntityNotFoundException("Пользователь с указанным ID не найден");
        }
        if (!filmRepository.filmExist(filmId)) {
            throw new EntityNotFoundException("Фильм с указанным ID не найден");
        }
        String sql = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";
        jdbc.update(sql, userId, filmId);
    }

    public List<Film> findPopularFilms(int size, int from) {
        return filmRepository.getSortedFilm(size, from);
    }

    // Метод для получения списка всех жанров
    public List<Genre> getGenres() {
        String sql = "SELECT * FROM genres";
        return jdbc.query(sql, new GenreRowMapper());
    }

    // Метод для получения жанра по id
    public Genre getGenreById(Long id) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?";
        if (!filmRepository.genreExist(id)) {
            throw new EntityNotFoundException("Жанр с указанным id не найден");
        }
        return jdbc.queryForObject(sql, new GenreRowMapper(), id);
    }

    // Метод для получения списка всех рейтингов
    public List<Mpa> getMpa() {
        String sql = "SELECT * FROM mpa";
        return jdbc.query(sql, new MpaRowMapper());
    }

    // Метод для получения рейтинга по id
    public Mpa getMpaById(Long id) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = ?";
        if (!filmRepository.mpaExist(id)) {
            throw new EntityNotFoundException("Рейтинг с указанным id не найден");
        }
        return jdbc.queryForObject(sql, new MpaRowMapper(), id);
    }

}
