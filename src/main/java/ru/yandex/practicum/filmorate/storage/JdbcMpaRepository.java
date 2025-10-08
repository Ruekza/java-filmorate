package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mappers.MpaRowMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcMpaRepository {
    private final JdbcTemplate jdbc;
    private final JdbcFilmRepository filmRepository;

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
