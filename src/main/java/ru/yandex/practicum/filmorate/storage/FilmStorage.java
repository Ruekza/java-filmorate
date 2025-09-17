package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    public Map<Long, Film> getFilms();

    public Film addFilm(Film film);

    public void deleteFilm(Long id);

    public Film updateFilm(Film newFilm);

    public List<Film> getAllFilms();

    public Film getFilmById(Long id);
}
