package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;

@Repository
@RequiredArgsConstructor
public class JdbcLikeRepository {
    private final JdbcTemplate jdbc;
    private final JdbcFilmRepository filmRepository;
    private final JdbcUserRepository userRepository;

    public void addLike(Long userId, Long filmId) {
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

}
