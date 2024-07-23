package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmStorage filmStorage;

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @PostMapping
    public Film filmAdd(@RequestBody Film filmRequest) {
        return filmStorage.filmAdd(filmRequest);
    }

    @PutMapping("{filmId}/like/{userId}")
    public void filmLike(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.filmLike(filmId, userId);
    }

    @DeleteMapping("{filmId}/like/{userId}")
    public void filmLikeRemove(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.filmLikeRemove(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> popularFilms(@RequestParam(defaultValue = "10") Long count) {
        return filmService.popularFilms(count);
    }

    @PutMapping
    public Film filmUpdate(@RequestBody Film newFilm) {
        return filmStorage.filmUpdate(newFilm);
    }

    @GetMapping
    public Collection<Film> allFilms() {
        return filmStorage.allFilms();
    }

    @DeleteMapping(value = {"{filmId}"})
    public void filmDelete(@PathVariable Long filmId) {
        filmStorage.filmDelete(filmId);
    }


}
