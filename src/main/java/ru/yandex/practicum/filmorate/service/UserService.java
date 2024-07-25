package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User userCreate(User newUser) {
        return userStorage.userCreate(newUser);
    }

    public Collection<User> allUsers() {
        return userStorage.allUsers();
    }

    public User userUpdate(User newUser) {
        return userStorage.userUpdate(newUser);
    }

    public void userDelete(Long id) {
        userStorage.userDelete(id);
    }

    public void addToFriend(Long userId, Long otherUserId) {
        User user = userStorage.getUser(userId);
        User otherUser = userStorage.getUser(otherUserId);
        if (user.getFriends().contains(otherUser)) {
            log.error("Ошибка при добавлении друга: Пользователи {} и {} уже являются друзьями.", userId, otherUserId);
            throw new ValidationException("Пользователи уже в друзьях");
        }
        user.getFriends().add(userStorage.getUser(otherUserId));
        otherUser.getFriends().add(userStorage.getUser(userId));
        log.info("Пользователь {} добавил в друзья пользователя {}", userId, otherUserId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user == null || friend == null) {
            log.error("Пользователь с ID {} не найден", userId);
        }

        if (user.getFriends().remove(friend) && friend.getFriends().remove(user)) {
            log.info("Пользователь {} успешно удалил пользователя {}", userId, friendId);
        } else {
            log.warn("Пользователь {} не имел друга с ID {}", userId, friendId);
        }
    }

    public List<User> allUserFriends(Long userId) {
        User user = userStorage.getUser(userId);
        Set<User> userFriendsSet = user.getFriends();
        return new ArrayList<>(userFriendsSet);
    }

    public List<User> commonFriends(Long userId, Long otherUserId) {
        List<User> userFriends = allUserFriends(userId);
        List<User> otherUserFriends = allUserFriends(otherUserId);
        List<User> commonFriends = new ArrayList<>(userFriends);
        commonFriends.retainAll(otherUserFriends);

        return commonFriends;
    }
}
