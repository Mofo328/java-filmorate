package ru.yandex.practicum.filmorate.repository.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    User addUser(User userRequest);

    User updateUser(User userRequest);

    Collection<User> allUsers();

    boolean userDelete(Long id);

    Optional<User> userGet(Long id);
}
