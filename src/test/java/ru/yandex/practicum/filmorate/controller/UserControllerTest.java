package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
public class UserControllerTest {


    @Autowired
    private UserController userController;

    @Test
    public void testUserCreate() {
        User newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setLogin("test_user");
        newUser.setName("Test User");
        newUser.setBirthday(LocalDate.of(2000, 1, 1));

        User createdUser = userController.userCreate(newUser);

        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals(newUser.getEmail(), createdUser.getEmail());
        assertEquals(newUser.getLogin(), createdUser.getLogin());
        assertEquals(newUser.getName(), createdUser.getName());
        assertEquals(newUser.getBirthday(), createdUser.getBirthday());
        System.out.println(userController.allUsers());
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test_user");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        userController.userCreate(user);

        User newUser = new User();
        newUser.setId(1L);
        newUser.setEmail("updated@example.com");
        newUser.setLogin("updated_user");
        newUser.setName("Updated User");
        newUser.setBirthday(LocalDate.of(1990, 5, 10));
        System.out.println(userController.allUsers());
        User updatedUser = userController.userUpdate(newUser);

        assertNotNull(updatedUser);
        assertEquals(newUser.getId(), updatedUser.getId());
        assertEquals(newUser.getEmail(), updatedUser.getEmail());
        assertEquals(newUser.getLogin(), updatedUser.getLogin());
        assertEquals(newUser.getName(), updatedUser.getName());
        assertEquals(newUser.getBirthday(), updatedUser.getBirthday());
    }

    @Test
    public void testAllUsers() {
        User newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setLogin("test_user");
        newUser.setName("Test User");
        newUser.setBirthday(LocalDate.of(2000, 1, 1));
        userController.userCreate(newUser);
        Collection<User> users = userController.allUsers();
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    public void testEmailIsCorrect() {
        User user = new User();
        user.setEmail("testexample.com");
        user.setLogin("test_user");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        User user2 = new User();
        user2.setEmail("");
        user2.setLogin("test_user");
        user2.setName("Test User");
        user2.setBirthday(LocalDate.of(2000, 1, 1));

        assertThrows(ValidationException.class, () -> userController.userCreate(user));
        assertThrows(ValidationException.class, () -> userController.userCreate(user2));
    }

    @Test
    public void testLoginUnCorrect() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        User user2 = new User();
        user2.setEmail("test@example.com");
        user2.setLogin("test user");
        user2.setName("Test User");
        user2.setBirthday(LocalDate.of(2000, 1, 1));

        assertThrows(ValidationException.class, () -> userController.userCreate(user));
        assertThrows(ValidationException.class, () -> userController.userCreate(user2));
    }

    @Test
    public void testNameCorrect() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test_user");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        User testUser = userController.userCreate(user);
        assertEquals(testUser.getName(), testUser.getLogin());
    }

    @Test
    public void testUnCorrectDateOfBirth() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test_user");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(2040, 1, 1));
        assertThrows(ValidationException.class, () -> userController.userCreate(user));
    }

    @Test
    public void testInvalidUserInputValues() {
        User newUser = new User();
        newUser.setEmail(null);
        newUser.setLogin("test_user");
        newUser.setName("Test User");
        newUser.setBirthday(LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> userController.userCreate(newUser));

        User newUser1 = new User();
        newUser1.setEmail("test@example.com");
        newUser1.setLogin(null);
        newUser1.setName("Test User");
        newUser1.setBirthday(LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> userController.userCreate(newUser1));

        User newUser3 = new User();
        newUser3.setEmail("test@example.com");
        newUser3.setLogin("test_user");
        newUser3.setName("Test User");
        newUser3.setBirthday(null);
        assertThrows(ValidationException.class, () -> userController.userCreate(newUser3));
    }
}