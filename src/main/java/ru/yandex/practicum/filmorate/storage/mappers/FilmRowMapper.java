package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.LinkedHashSet;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(Duration.ofSeconds(rs.getInt("duration")));

        Mpa newMpa = new Mpa();
        newMpa.setId(rs.getLong("mpa_id"));
        film.setMpa(newMpa);

        film.setGenres(new LinkedHashSet<>());

        Genre newGenre = new Genre();
        newGenre.setId(rs.getLong("genre_id"));
        film.getGenres().add(newGenre);

        return film;
    }

}
