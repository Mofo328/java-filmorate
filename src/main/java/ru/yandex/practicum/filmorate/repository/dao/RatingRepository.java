package ru.yandex.practicum.filmorate.repository.dao;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingRepository {

    Optional<Rating> findRatingById(Long id);

    List<Rating> getAllMpa();
}
