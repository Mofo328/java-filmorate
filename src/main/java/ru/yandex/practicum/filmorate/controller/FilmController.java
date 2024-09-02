package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.util.Collection;


@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    private final ValidateService validateService;

    public FilmController(FilmService filmService, ValidateService validateService) {
        this.filmService = filmService;
        this.validateService = validateService;
    }

    @PostMapping
    public Film filmAdd(@RequestBody Film filmRequest) {
        validateService.validateFilmInput(filmRequest);
        return filmService.filmAdd(filmRequest);
    }

    @PutMapping("{filmId}/like/{userId}")
    public boolean filmLike(@PathVariable Long filmId, @PathVariable Long userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("{filmId}/like/{userId}")
    public boolean filmLikeRemove(@PathVariable Long filmId, @PathVariable Long userId) {
        return filmService.likeRemove(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> popularFilms(@RequestParam(defaultValue = "10") Long count) {
        return filmService.popularFilms(count);
    }

    @PutMapping
    public Film filmUpdate(@RequestBody Film newFilm) {
        validateService.validateFilmUpdate(newFilm);
        return filmService.filmUpdate(newFilm);
    }

    @GetMapping
    public Collection<Film> allFilms() {
        return filmService.allFilms();
    }

    @GetMapping(value = {"{filmId}"})
    public Film filmByID(@PathVariable Long filmId) {
        return filmService.getFilm(filmId);
    }

    @DeleteMapping(value = {"{filmId}"})
    public boolean filmDelete(@PathVariable Long filmId) {
        return filmService.filmDelete(filmId);
    }
}
