package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository {
    private final JdbcTemplate jdbc;
    private final JdbcFilmRepository filmRepository;

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
}
