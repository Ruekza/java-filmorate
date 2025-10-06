package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class FilmListExtractor implements ResultSetExtractor<List<Film>> {
    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Film> films = new ArrayList<>();
        Long currentFilmId = null;
        Film currentFilm = null;

        while (rs.next()) {
            Long filmId = rs.getLong("film_id");
            if (currentFilmId == null || !currentFilmId.equals(filmId)) {
                if (currentFilm != null) {
                    films.add(currentFilm);
                }
                currentFilmId = filmId;
                currentFilm = new Film();

                currentFilm.setId(rs.getLong("film_id"));
                currentFilm.setName(rs.getString("name"));
                currentFilm.setDescription(rs.getString("description"));
                currentFilm.setReleaseDate(rs.getDate("release_date").toLocalDate());
                currentFilm.setDuration(Duration.ofSeconds(rs.getInt("duration")));

                Mpa newMpa = new Mpa();
                newMpa.setId(rs.getLong("mpa_id"));

                currentFilm.setMpa(newMpa);

                currentFilm.setGenres(new LinkedHashSet<>());
            }
            Genre newGenre = new Genre();
            newGenre.setId(rs.getLong("genre_id"));

            currentFilm.getGenres().add(newGenre);

        }
        if (currentFilm != null) {
            films.add(currentFilm);
        }
        return films;
    }

}
