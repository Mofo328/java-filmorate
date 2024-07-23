package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film filmAdd(Film film);

    Film filmUpdate(Film film);

    Collection<Film> allFilms();

    void filmDelete(Long id);

    Film getFilm(Long id);
}
