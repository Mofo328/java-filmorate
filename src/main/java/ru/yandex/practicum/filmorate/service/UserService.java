package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.dao.FriendRepository;
import ru.yandex.practicum.filmorate.repository.dao.UserRepository;

import java.util.*;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final FriendRepository friendRepository;

    @Autowired
    public UserService(UserRepository userRepository, FriendRepository friendRepository) {
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
    }

    public User userCreate(User newUser) {
        log.info("Отправлен ответ Put / users с телом {}", newUser);
        return userRepository.addUser(newUser);
    }

    public Collection<User> allUsers() {
        log.info("Отправлен ответ GET /users");
        return userRepository.allUsers();
    }

    public User userUpdate(User newUser) {
        log.info("Отправлен ответ Put / users с телом {}", newUser);
        return userRepository.updateUser(newUser);
    }

    public boolean userDelete(Long id) {
        log.info("Пользователь c ID {} удален", id);
        return userRepository.userDelete(id);
    }

    public boolean addFriend(Long userId, Long friendId) {
        log.info("Пользователи с ID {},{} теперь друзья", userId, friendId);
        return friendRepository.addFriend(userId, friendId);
    }

    public boolean friendRemove(Long userId, Long friendId) {
        log.info("Пользователи с ID {},{} теперь недрузья", userId, friendId);
        return friendRepository.friendRemove(userId, friendId);
    }


    public Collection<User> userFriends(Long userId) {
        log.info("Отправлен ответ GET /users/friends");
        return friendRepository.userFriends(userId);
    }


    public Collection<User> commonFriends(Long userIntersectionId, Long userId) {
        log.info("Отправлен ответ GET users/friends/common");
        return friendRepository.commonFriends(userIntersectionId, userId);
    }
}
