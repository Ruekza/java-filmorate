package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Getter
public class InMemoryFilmStorage implements FilmStorage {
    private long generatorId = 0;
    protected final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> getSortedFilm(int size, int from) {
        Comparator<Film> filmComparator = Comparator.comparing(
                film -> Optional.ofNullable(film.getLikes()).orElse(Collections.emptySet()).size()
        );
        return films.values().stream()
                .sorted(filmComparator.reversed())
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void deleteFilm(Long filmId) {
        if (films.containsKey(filmId)) {
            films.remove(filmId);
        } else {
            throw new EntityNotFoundException("Фильм с указанным ID не найден");
        }
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            return oldFilm;
        } else {
            throw new EntityNotFoundException("Фильм с указанным ID не найден");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Long id) {
        Film film = films.get(id);
        if (film == null) {
            throw new EntityNotFoundException("Фильм с указанным ID не найден");
        }
        return films.get(id);
    }

    private long getNextId() {
        return ++generatorId;
    }
}
