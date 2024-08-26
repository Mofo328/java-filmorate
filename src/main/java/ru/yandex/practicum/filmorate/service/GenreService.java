package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.dao.GenreRepository;

import java.util.List;

@Service
@Slf4j
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Genre genreById(Long id) {
        try {
            return genreRepository.genreById(id).orElseThrow(() ->
                    new ConditionsNotMetException("Жанра с ID " + id + " не существует"));
        } catch (EmptyResultDataAccessException ignored) {
            throw new ConditionsNotMetException("Жанра с ID " + id + " не существует");
        }
    }

    public List<Genre> getAllGenre() {
        log.info("Отправлен ответ GET /genres");
        return genreRepository.getAllGenre();
    }
}
