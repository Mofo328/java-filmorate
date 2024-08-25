package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.dao.FilmRepository;
import ru.yandex.practicum.filmorate.repository.dao.LikeRepository;

import java.util.*;


@Service
@Slf4j
public class FilmService {

    private final FilmRepository filmRepository;

    private final LikeRepository likeRepository;

    @Autowired
    public FilmService(FilmRepository filmRepository, LikeRepository likeRepository) {
        this.filmRepository = filmRepository;
        this.likeRepository = likeRepository;
    }

    public Film filmAdd(Film filmRequest) {
        log.info("Отправлен ответ Put / films с телом {}", filmRequest);
        return filmRepository.filmAdd(filmRequest);
    }

    public Collection<Film> allFilms() {
        log.info("Отправлен ответ GET /films");
        return filmRepository.allFilms();
    }

    public Film filmUpdate(Film filmUpdate) {
        log.info("Отправлен ответ Put / films с телом {}", filmUpdate);
        return filmRepository.filmUpdate(filmUpdate);
    }

    public boolean filmDelete(Long id) {
        log.info("Фильм удален c ID {} удален", id);
        return filmRepository.filmDelete(id);
    }

    public Film getFilm(Long id) {
        Optional<Film> filmOptional = filmRepository.getFilm(id);
        if (filmOptional.isPresent()) {
            log.info("Отправлен ответ GET / films с телом {}", filmOptional.get());
            return filmOptional.get();
        } else {
            log.error("Такого фильма не существует");
            throw new ConditionsNotMetException("Фильма с ID " + id + " не существует");
        }
    }

    public Collection<Film> popularFilms(Long count) {
        log.info("Было возращено {} популярных фильма", count);
        return filmRepository.popularFilms(count);
    }

    public boolean likeRemove(Long filmId, Long userId) {
        log.info("У фильма {} был удален лайк от пользователя {}", filmId, userId);
        return likeRepository.likeRemove(filmId, userId);
    }

    public boolean addLike(Long filmId, Long userId) {
        log.info("Фильму {} был поставлен лайк от пользователя {}", filmId, userId);
        return likeRepository.addLike(filmId, userId);
    }
}
