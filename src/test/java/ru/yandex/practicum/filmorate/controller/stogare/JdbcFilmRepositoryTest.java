package ru.yandex.practicum.filmorate.controller.stogare;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.JdbcFilmRepository;

import java.time.Duration;
import java.time.LocalDate;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({JdbcFilmRepository.class})
public class JdbcFilmRepositoryTest {
    private final JdbcFilmRepository filmRepository;

    @Test
    public void testAddFilm() {
        Film film = new Film(null, "film1", "desc1", LocalDate.of(2001, 01, 01), Duration.ofMinutes(180), null, null);
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        mpa.setName("G");
        film.setMpa(mpa);
        Film createdFilm = filmRepository.addFilm(film);
        Film gotFilm = filmRepository.getFilmById(createdFilm.getId());
        Assertions.assertNotNull(createdFilm);
        Assertions.assertEquals(gotFilm.getId(), createdFilm.getId());
        Assertions.assertEquals("film1", createdFilm.getName());
        Assertions.assertEquals("desc1", createdFilm.getDescription());
        Assertions.assertEquals(LocalDate.of(2001, 01, 01), createdFilm.getReleaseDate());
        Assertions.assertEquals(Duration.ofMinutes(180), createdFilm.getDuration());
        Assertions.assertEquals("G", createdFilm.getMpa().getName());
    }

    @Test
    public void testGetFilmById() {
        Film film = new Film(null, "film", "desc", LocalDate.of(1995, 12, 28), Duration.ofMinutes(180), null, null);
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        mpa.setName("G");
        film.setMpa(mpa);
        Film createdFilm = filmRepository.addFilm(film);
        Film gotFilm = filmRepository.getFilmById(createdFilm.getId());
        Assertions.assertNotNull(gotFilm);
        Assertions.assertEquals(createdFilm.getId(), gotFilm.getId());
    }

    @Test
    public void testDeleteFilm() {
        Film film = new Film(null, "film2", "desc2", LocalDate.of(2005, 12, 28), Duration.ofMinutes(180), null, null);
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        mpa.setName("G");
        film.setMpa(mpa);
        Film createdFilm = filmRepository.addFilm(film);
        Film gotFilm = filmRepository.getFilmById(createdFilm.getId());
        Assertions.assertNotNull(gotFilm);
        filmRepository.deleteFilm(gotFilm.getId());
        Assertions.assertNull(filmRepository.getFilmById(gotFilm.getId()));
    }

}
