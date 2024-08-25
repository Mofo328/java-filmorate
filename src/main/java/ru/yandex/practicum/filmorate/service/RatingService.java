package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.dao.RatingRepository;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating findRatingById(Long id) {
        Optional<Rating> ratingOptional = ratingRepository.findRatingById(id);
        if (ratingOptional.isPresent()) {
            log.info("Отправлен ответ GET /mpa с телом {}", ratingOptional.get());
            return ratingOptional.get();
        } else {
            log.error("Такого mpa не существует");
            throw new ConditionsNotMetException("Mpa с ID " + id + " не существует");
        }
    }

    public List<Rating> getAllMpa() {
        log.info("Отправлен ответ GET /mpa");
        return ratingRepository.getAllMpa();
    }
}
