package ru.yandex.practicum.filmorate.repository.dao;

import ru.yandex.practicum.filmorate.model.Genre;


import java.util.List;
import java.util.Optional;

public interface GenreRepository {

    List<Genre> getAllGenre();

    Optional<Genre> genreById(Long id);
}
