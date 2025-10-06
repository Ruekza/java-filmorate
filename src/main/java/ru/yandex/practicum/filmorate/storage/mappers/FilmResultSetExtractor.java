package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class FilmResultSetExtractor implements ResultSetExtractor<Film> {
    @Override
    public Film extractData(ResultSet rs) throws SQLException, DataAccessException {
        Film film = null;
        while (rs.next()) {
            if (film == null) {
                film = new Film();
                // заполнить поля объекта film данными из ResultSet
                film.setId(rs.getLong("film_id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                film.setDuration(Duration.ofSeconds(rs.getInt("duration")));

                Mpa newMpa = new Mpa();
                newMpa.setId(rs.getLong("mpa_id"));
                newMpa.setName(rs.getString("mpa_name"));
                film.setMpa(newMpa);
            }
            // Обрабока лайков
            if (film.getLikes() == null) {
                film.setLikes(new HashSet<>());
            }
            film.getLikes().add(rs.getLong("user_id"));

            // Обработка жанров
            if (film.getGenres() == null) {
                film.setGenres(new LinkedHashSet<>());
            }
            if (rs.getLong("genre_id") != 0 && rs.getString("genre_name") != null) {
                Genre newGenre = new Genre(); //
                newGenre.setId(rs.getLong("genre_id"));
                newGenre.setName(rs.getString("genre_name"));//
                film.getGenres().add(newGenre);  //
            }
        }
        return film;
    }

}
