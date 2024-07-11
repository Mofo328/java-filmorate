package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film filmAdd(@RequestBody Film filmRequest) {
        validateFilmInput(filmRequest);
        Film film = setFilm(filmRequest);
        films.put(film.getId(), film);
        log.info("Фильм добавлен");
        return film;
    }

    @PutMapping
    public Film filmUpdate(@RequestBody Film newFilm) {
        if (newFilm.getId() == null || !films.containsKey(newFilm.getId())) {
            log.error("Фильм с id " + newFilm.getId() + " не найден");
            throw new ConditionsNotMetException("id не найден");
        }
        Film oldFilm = setFilm(newFilm);
        validateFilmInput(newFilm);
        log.info("Фильм обнавлен");
        return oldFilm;
    }

    @GetMapping
    public Collection<Film> allFilms() {
        log.info("Все фильмы");
        return films.values();
    }

    private Film setFilm(Film filmRequest) {
        Film film = new Film();
        if (films.get(filmRequest.getId()) == null) {
            film.setId(getNextId());
        }
        if (films.get(filmRequest.getId()) != null) {
            film.setId(filmRequest.getId());
        }
        film.setName(filmRequest.getName());
        film.setDescription(filmRequest.getDescription());
        film.setReleaseDate(filmRequest.getReleaseDate());
        film.setDuration(filmRequest.getDuration());
        return film;
    }

    private void validateFilmInput(Film film) {
        if (film.getName() == null || film.getName().trim().isEmpty()) {
            log.error("Ошибка в названии фильма!");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Превышена длина описания!");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(minReleaseDate)) {
            log.error("Ошибка в дате релиза!");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.error("Продолжительность отрицательная!");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
