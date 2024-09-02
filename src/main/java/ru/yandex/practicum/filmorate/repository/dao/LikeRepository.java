package ru.yandex.practicum.filmorate.repository.dao;

public interface LikeRepository {

    boolean addLike(Long filmId, Long userId);

    boolean likeRemove(Long filmId, Long userId);
}
