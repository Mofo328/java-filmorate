package ru.yandex.practicum.filmorate.repository.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendRepository {

    boolean addFriend(Long userId, Long friendId);

    boolean friendRemove(Long userId, Long friendId);

    Collection<User> commonFriends(Long userIntersectionId, Long userId);

    Collection<User> userFriends(Long userId);
}
