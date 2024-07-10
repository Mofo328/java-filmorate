package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.Duration;
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
        Film film = new Film();
        film.setId(getNextId());
        film.setName(filmRequest.getName());
        film.setDescription(filmRequest.getDescription());
        film.setReleaseDate(filmRequest.getReleaseDate());
        film.setDuration(filmRequest.getDuration());
        films.put(film.getId(), film);
        log.info("Фильм добавлен");
        return film;
    }

    @PutMapping
    public Film filmUpdate(@RequestBody Film newFilm) {
        Film oldFilm;
        if (newFilm.getId() == null || !films.containsKey(newFilm.getId())) {
            log.error("Фильм с id " + newFilm.getId() + " не найден");
            throw new ConditionsNotMetException("id не найден");
        }
        oldFilm = films.get(newFilm.getId());
        validateFilmInput(newFilm);
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        log.info("Фильм обнавлен");
        return oldFilm;
    }

    @GetMapping
    public Collection<Film> allFilms() {
        return films.values();
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private Duration convertDurationToSeconds(int duration) {
        Duration dur = Duration.ofSeconds(duration);
        return dur;
    }

    private void validateFilmInput(Film film){
        if (film.getName().isEmpty() || (film.getDescription().length() > 200) ||
                !film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))
                || !convertDurationToSeconds(film.getDuration()).isPositive()) {
            log.error("Фильм не соответсвует условиям");
            throw new ValidationException("Валидация не пройдена.");
        }
    }
}
