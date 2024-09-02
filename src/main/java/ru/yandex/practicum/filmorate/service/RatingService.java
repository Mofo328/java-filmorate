package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.dao.RatingRepository;

import java.util.List;


@Service
@Slf4j
public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating findRatingById(Long id) {
        try {
            return ratingRepository.findRatingById(id).orElseThrow(() ->
                    (new ConditionsNotMetException("Mpa с ID " + id + " не существует")));
        } catch (EmptyResultDataAccessException ignored) {
            throw new ConditionsNotMetException("Mpa с ID " + id + " не существует");
        }
    }

    public List<Rating> getAllMpa() {
        log.info("Отправлен ответ GET /mpa");
        return ratingRepository.getAllMpa();
    }
}
