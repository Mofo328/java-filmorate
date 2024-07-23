package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film filmAdd(Film filmRequest) {
        return filmStorage.filmAdd(filmRequest);
    }

    public Collection<Film> allFilms() {
        return filmStorage.allFilms();
    }

    public Film filmUpdate(Film newFilm) {
        return filmStorage.filmUpdate(newFilm);
    }

    public void filmDelete(Long id) {
        filmStorage.filmDelete(id);
    }

    public void filmLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        Set<Long> likes = film.getLikes();
        if (likes.contains(userId)) {
            log.error("Пользователь {} уже поставил лайк фильму {}", userId, filmId);
            throw new ValidationException("Пользователь уже ставил лайк этому фильму");
        }
        User user = userStorage.getUser(userId);
        likes.add(user.getId());
        log.info("Фильму {} был поставлен лайк от пользователя {}", filmId, userId);
    }

    public void filmLikeRemove(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        Set<Long> likes = film.getLikes();
        User user = userStorage.getUser(userId);
        if (!likes.remove(user.getId())) {
            throw new ConditionsNotMetException("Пользователь " + userId + " Не ставил лайк этому фильму");
        }
        log.info("У фильма {} был удален лайк от пользователя {}", filmId, userId);
    }

    public Collection<Film> popularFilms(Long count) {
        log.info("Было возращено {} популярных фильма", count);
        return filmStorage.allFilms().stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList()).reversed();
    }
}
