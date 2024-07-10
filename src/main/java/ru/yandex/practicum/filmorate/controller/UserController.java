package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User userCreate(@RequestBody User newUser) {
        validateUserInput(newUser);
        if (newUser.getName() == null) {
            User userNoName = new User();
            userNoName.setId(getNextId());
            userNoName.setEmail(newUser.getEmail());
            userNoName.setLogin(newUser.getLogin());
            userNoName.setName(newUser.getLogin());
            userNoName.setBirthday(newUser.getBirthday());
            users.put(userNoName.getId(), userNoName);
            log.info("Пользователь добавлен");
            return userNoName;
        }
        User user = new User();
        user.setId(getNextId());
        user.setEmail(newUser.getEmail());
        user.setLogin(newUser.getLogin());
        user.setName(newUser.getName());
        user.setBirthday(newUser.getBirthday());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }


    @PutMapping
    public User updateUser(@RequestBody User newUser) {
        User oldUser;
        if (newUser.getId() == null || !users.containsKey(newUser.getId())) {
            log.error("Пользователь с id " + newUser.getId() + " не найден");
            throw new ConditionsNotMetException("id не найден");
        }
        oldUser = users.get(newUser.getId());
        validateUserInput(newUser);
        if (newUser.getName().isEmpty()) {
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("Пользователь обнавлен");
            return oldUser;
        }
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setName(newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        log.info("Пользователь обнавлен");
        return oldUser;
    }

    @GetMapping
    public Collection<User> allUsers() {
        return users.values();
    }

    private void validateUserInput(User newUser) {
        if (newUser.getEmail().isEmpty() || !newUser.getEmail().contains("@")
                || newUser.getLogin().isEmpty() || newUser.getLogin().contains(" ")
                || newUser.getBirthday().isAfter(LocalDate.now())) {
            log.error("Пользователь не соответсвует условиям");
            throw new ValidationException("Валидация не пройдена");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
