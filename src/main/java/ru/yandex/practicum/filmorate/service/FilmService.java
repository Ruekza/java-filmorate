package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.*;


@Service
public class FilmService {
    private final JdbcFilmRepository filmRepository;
    private final JdbcUserRepository userRepository;
    private final JdbcLikeRepository likeRepository;
    private final JdbcGenreRepository genreRepository;
    private final JdbcMpaRepository mpaRepository;

    @Autowired
    public FilmService(JdbcFilmRepository filmRepository, JdbcUserRepository userRepository, JdbcLikeRepository likeRepository, JdbcGenreRepository genreRepository, JdbcMpaRepository mpaRepository) {
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.genreRepository = genreRepository;
        this.mpaRepository = mpaRepository;
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
        likeRepository.addLike(userId, filmId);
    }

    public void deleteLike(Long userId, Long filmId) {
        likeRepository.deleteLike(userId, filmId);
    }

    public List<Film> findPopularFilms(int size, int from) {
        return filmRepository.getSortedFilm(size, from);
    }

    public List<Genre> getGenres() {
        return genreRepository.getGenres();
    }

    public Genre getGenreById(Long id) {
        return genreRepository.getGenreById(id);
    }

    public List<Mpa> getMpa() {
        return mpaRepository.getMpa();
    }

    public Mpa getMpaById(Long id) {
        return mpaRepository.getMpaById(id);
    }

}
