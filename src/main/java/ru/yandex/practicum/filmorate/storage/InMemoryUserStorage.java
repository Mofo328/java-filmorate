package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

//    private final Map<Long,User>

    private long currentMaxId;

    @Override
    public User userCreate(User newUser) {
        log.info("Пришел Post запрос /users с телом {}", newUser);
        validateUserInput(newUser);
        newUser.setId(getNextId());
        users.put(newUser.getId(), newUser);
        log.info("Отправлен ответ Post /users с телом {}", newUser);
        return newUser;
    }

    @Override
    public Collection<User> allUsers() {
        log.info("Все пользователи");
        return users.values();
    }

    @Override
    public User userUpdate(User newUser) {
        log.info("Пришел Put запрос /users с телом {}", newUser);
        if (newUser.getId() == null || !users.containsKey(newUser.getId())) {
            log.error("Пользователь с id " + newUser.getId() + " не найден");
            throw new ConditionsNotMetException("id не найден");
        }
        User oldUser = users.get(newUser.getId());
        validateUserInput(newUser);
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setName(newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        if (oldUser.getName() == null) {
            oldUser.setName(oldUser.getLogin());
        }
        log.info("Отправлен ответ Put /users с телом {}", oldUser);
        return oldUser;
    }

    @Override
    public User getUser(Long id) {
        if (users.get(id) == null) {
            throw new ConditionsNotMetException("id не найден");
        }
        return users.get(id);
    }

    @Override
    public void userDelete(Long id) {
        if (users.get(id) == null) {
            throw new ConditionsNotMetException("id не найден");
        }
        log.info("Пользователь удален");
        users.remove(id);
    }

    private void validateUserInput(User user) {
        if (user.getLogin() == null || user.getEmail() == null || user.getBirthday() == null) {
            log.error("Ошибка в запросе");
            throw new ValidationException("Переданно нулевое значение");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Ошибка в написании почты");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Ошибка в написании логина");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка в дате рождения");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    private long getNextId() {
        return ++currentMaxId;
    }
}
