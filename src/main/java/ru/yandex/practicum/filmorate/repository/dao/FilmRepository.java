package ru.yandex.practicum.filmorate.repository.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmRepository {
    Film filmAdd(Film filmRequest);

    Film filmUpdate(Film filmRequest);

    Collection<Film> allFilms();

    Collection<Film> popularFilms(Long count);

    Optional<Film> getFilm(Long id);

    boolean filmDelete(Long id);
}
