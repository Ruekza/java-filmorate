package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.FilmListExtractor;
import ru.yandex.practicum.filmorate.storage.mappers.FilmResultSetExtractor;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;


@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmStorage {
    private final JdbcTemplate jdbc;

    @Override
    public Map<Long, Film> getFilms() {
        return Map.of();
    }

    @Override
    public List<Film> getSortedFilm(int size, int from) {
        String sql = "SELECT f.*, m.mpa_id, g.genre_id, COUNT(likes.user_id) FROM films AS f "
                + "LEFT JOIN films_mpa AS m ON f.film_id = m.film_id "
                + "LEFT JOIN films_genres AS g ON f.film_id = g.film_id "
                + "LEFT JOIN likes ON f.film_id = likes.film_id GROUP BY f.film_id, g.genre_id ORDER BY COUNT(likes.user_id) DESC LIMIT ? OFFSET ?";
        return jdbc.query(sql, new FilmListExtractor(), size, from);
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration) " +
                "VALUES (?, ?, ?, ?)";
        Long id = insert(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        film.setId(id);
        // Вставляем или обновляем рейтинг
        String mpaSql = "INSERT INTO films_mpa (film_id, mpa_id) VALUES (?, ?)";
        if (!mpaExist(film.getMpa().getId())) {
            throw new EntityNotFoundException("Рейтинг с указанным id не найден");
        }
        jdbc.update(mpaSql, film.getId(), film.getMpa().getId());
        // Вставляем или обновляем жанры

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                String genreSql = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
                if (!genreExist(genre.getId())) {
                    throw new EntityNotFoundException("Жанр с указанным id не найден");
                }
                jdbc.update(genreSql, film.getId(), genre.getId());
            }
        }
        return film;
    }

    @Override
    public void deleteFilm(Long id) {
        String sqlQuery = "DELETE FROM films WHERE film_id = ?";
        jdbc.update(sqlQuery, id);
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (!filmExist(newFilm.getId())) {
            throw new EntityNotFoundException("Фильм с указанным id не найден");
        }
        String filmSqlQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ? WHERE film_id = ?";
        String mpaSqlQuery = "UPDATE films_mpa SET mpa_id = ? WHERE film_id = ?";
        // удаляем все жанры для фильма
        String deleteGenresSqlQuety = "DELETE FROM films_genres WHERE film_id = ?";
        jdbc.update(deleteGenresSqlQuety, newFilm.getId());
        // обновляем фильм и рейтинг
        jdbc.update(filmSqlQuery, newFilm.getName(), newFilm.getDescription(), newFilm.getReleaseDate(),
                newFilm.getDuration(), newFilm.getId());
        jdbc.update(mpaSqlQuery, newFilm.getMpa().getId(), newFilm.getId());
        // вставляем новые жанры
        if (newFilm.getGenres() == null) {
            newFilm.setGenres(new LinkedHashSet<>());
        }
        for (Genre genre : newFilm.getGenres()) {
            String genresSqlQuery = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
            jdbc.update(genresSqlQuery, newFilm.getId(), genre.getId());
        }
        return newFilm;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT f.*, m.mpa_id, g.genre_id FROM films AS f " +
                "JOIN films_mpa AS m ON f.film_id = m.film_id " +
                "LEFT JOIN films_genres AS g ON f.film_id = g.film_id " +
                "ORDER BY f.film_id";
        return jdbc.query(sql, new FilmListExtractor());
    }

    @Override
    public Film getFilmById(Long id) {
        String sql = "SELECT f.*, m.mpa_id, mpa.name AS mpa_name, g.genre_id, genres.name AS genre_name, l.user_id FROM films AS f " +
                "JOIN films_mpa AS m ON f.film_id = m.film_id " +
                "JOIN mpa ON m.mpa_id = mpa.mpa_id " +
                "LEFT JOIN films_genres AS g ON f.film_id = g.film_id " +
                "LEFT JOIN genres ON g.genre_id = genres.genre_id " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id WHERE f.film_id = ?";
        return jdbc.query(sql, new FilmResultSetExtractor(), id);
    }

    public boolean filmExist(Long id) {
        String sql = "SELECT COUNT(*) FROM films WHERE film_id = ?";
        return jdbc.queryForObject(sql, Boolean.class, id);
    }

    public boolean mpaExist(Long id) {
        String sql = "SELECT COUNT(*) FROM mpa WHERE mpa_id = ?";
        return jdbc.queryForObject(sql, Boolean.class, id);
    }

    public boolean genreExist(Long id) {
        String sql = "SELECT COUNT(*) FROM genres WHERE genre_id = ?";
        return jdbc.queryForObject(sql, Boolean.class, id);
    }

    protected Long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        // Возвращаем id нового фильма
        if (id != null) {
            return id;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

}
