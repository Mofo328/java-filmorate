package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;


    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriend(Long userId, Long otherUserId) {
        User user = userStorage.getUser(userId);
        User otherUser = userStorage.getUser(otherUserId);
        Set<Long> userFriends = user.getFriends();
        Set<Long> otherUserFriends = otherUser.getFriends();
        if (userFriends == null) {
            userFriends = new HashSet<>();
        }
        if (otherUserFriends == null) {
            otherUserFriends = new HashSet<>();
        }
        if (userFriends.contains(otherUserId)) {
            log.error("Пользователь {} уже есть в друзьях у пользователя {}", userId, otherUserId);
            throw new ValidationException("Пользователь уже в друзьях");
        }
        userFriends.add(otherUserId);
        otherUserFriends.add(userId);
        user.setFriends(userFriends);
        otherUser.setFriends(otherUserFriends);
        log.info("Пользователь {} добавил в друзья пользователя {}", userId, otherUserId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        Set<Long> friends = user.getFriends();
        if (!friends.remove(friendId)) {
            throw new ConditionsNotMetException("У пользователя нет друга под id " + friendId);
        }

        log.info("Пользователь {} удалил  пользователя {}", userId, friendId);
        user.setFriends(friends);
    }

    public Set<Long> allUserFriends(Long userId) {
        User user = userStorage.getUser(userId);
        return user.getFriends();
    }

    public Set<Long> commonFriends(Long userId, Long otherUserId) {
        Set<Long> userFriends = allUserFriends(userId);
        Set<Long> otherUserFriends = allUserFriends(otherUserId);
        Set<Long> commonFriends = new HashSet<>(userFriends);
        commonFriends.retainAll(otherUserFriends);
        return commonFriends;
    }
}
