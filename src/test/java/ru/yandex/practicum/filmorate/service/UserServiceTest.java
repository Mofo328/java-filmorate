package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private final UserStorage userStorage = new InMemoryUserStorage();

    private final UserService userService = new UserService(userStorage);


    private User user;
    private User user2;
    private User user3;
    private User user4;

    private User user5;

    @BeforeEach
    public void setupUsers() {
        user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test_user");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        userStorage.userCreate(user);

        user2 = new User();
        user2.setEmail("2test2@example.com");
        user2.setLogin("2test2_user2");
        user2.setName("2Test2 2Use2r");
        user2.setBirthday(LocalDate.of(2002, 1, 1));
        userStorage.userCreate(user2);

        user3 = new User();
        user3.setEmail("3test3@example.com");
        user3.setLogin("3test3_3user3");
        user3.setName("3Test3 3User3");
        user3.setBirthday(LocalDate.of(2003, 1, 1));
        userStorage.userCreate(user3);

        user4 = new User();
        user4.setEmail("4test@example4.com");
        user4.setLogin("4test_user4");
        user4.setName("4Test User4");
        user4.setBirthday(LocalDate.of(2004, 1, 1));
        userStorage.userCreate(user4);

        user5 = new User();
        user5.setEmail("5test@example5.com");
        user5.setLogin("5test_user5");
        user5.setName("5Test User5");
        user5.setBirthday(LocalDate.of(2005, 1, 1));
        userStorage.userCreate(user5);
    }

    @Test
    public void canUserAddFriendAndDelete() {
        //добавил к юзеру1,юзера2
        userService.addToFriend(user.getId(), user2.getId());
        //пытаюсь повтроно добавить к юзеру1,юзера2
        assertThrows(ValidationException.class, () -> userService.addToFriend(user.getId(), user2.getId()));
        userService.addToFriend(user.getId(), user3.getId());
        //проверяю добивился ли в друзья к пользователям 2,3- первый пользователь
        assertTrue(user2.getFriends().contains(user.getId()));
        assertTrue(user3.getFriends().contains(user.getId()));
        assertEquals(2, userService.allUserFriends(user.getId()).size());
        //проверяю удаление пользователя
        userService.deleteFriend(user.getId(), user2.getId());
        //проверяю повторное удаление пользователя
        assertThrows(ConditionsNotMetException.class, () -> userService.deleteFriend(user.getId(), user2.getId()));
        assertEquals(1, userService.allUserFriends(user.getId()).size());
    }

    @Test
    public void hasUserCommonFriends() {
        userService.addToFriend(user.getId(), user2.getId());
        userService.addToFriend(user.getId(), user3.getId());
        userService.addToFriend(user3.getId(), user4.getId());
        userService.addToFriend(user4.getId(), user.getId());
        userService.addToFriend(user5.getId(), user2.getId());
        userService.addToFriend(user5.getId(), user3.getId());
        userService.addToFriend(user5.getId(), user.getId());
        Set<Long> expected = Set.of(user5.getId(), user4.getId());
        assertEquals(expected, userService.commonFriends(user.getId(), user3.getId()));
    }
}