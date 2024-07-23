package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("{userId}/friends/{friendId}")
    public void addToFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.addToFriend(userId, friendId);
    }

    @DeleteMapping("{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("{userId}/friends")
    public Set<Long> allUserFriends(@PathVariable Long userId) {
        return userService.allUserFriends(userId);
    }

    @GetMapping("{userId}/friends/common/{otherUserId}")
    public Set<Long> commonFriends(@PathVariable Long userId, @PathVariable Long otherUserId) {
        return userService.commonFriends(userId, otherUserId);
    }

    @PostMapping
    public User userCreate(@RequestBody User newUser) {
        return userService.userCreate(newUser);
    }

    @PutMapping
    public User userUpdate(@RequestBody User newUser) {
        return userService.userUpdate(newUser);
    }

    @GetMapping
    public Collection<User> allUsers() {
        return userService.allUsers();
    }

    @DeleteMapping(value = {"{userId}"})
    public void userDelete(@PathVariable("userId") Long id) {
        userService.userDelete(id);
    }
}
