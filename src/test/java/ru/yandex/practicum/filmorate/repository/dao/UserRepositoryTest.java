package ru.yandex.practicum.filmorate.repository.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ImportResource
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FriendRepository friendRepository;

    @Test
    void shouldGetUserByIdTest() {
        Optional<User> userOptional = userRepository.userGet(1L);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(user).hasFieldOrPropertyWithValue("login", "Surname1");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "User1");
                            assertThat(user).hasFieldOrPropertyWithValue("email", "mail1@yandex.ru");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                    LocalDate.of(2020, 1, 1));
                        }
                );
    }

    @Test
    void shouldGetAllUsersTest() {
        Optional<Collection<User>> userList = Optional.ofNullable(userRepository.allUsers());
        assertThat(userList)
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user).isNotEmpty();
                    assertThat(user).hasSize(4);
                    assertThat(user).element(0).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(user).element(1).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(user).element(2).hasFieldOrPropertyWithValue("id", 3L);
                    assertThat(user).element(3).hasFieldOrPropertyWithValue("id", 4L);
                });
    }

    @Test
    void shouldCreateUserTest() {
        User newUser = new User();
        newUser.setLogin("Surname5");
        newUser.setName("user5");
        newUser.setEmail("mail5@yandex.com");
        newUser.setBirthday(LocalDate.of(2020, 8, 19));

        Optional<User> userOptional = Optional.ofNullable(userRepository.addUser(newUser));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 5L);
                            assertThat(user).hasFieldOrPropertyWithValue("login", "Surname5");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "user5");
                            assertThat(user).hasFieldOrPropertyWithValue("email",
                                    "mail5@yandex.com");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                    LocalDate.of(2020, 8, 19));
                        }
                );
    }

    @Test
    void shouldUpdateUserTest() {
        User newUser = new User();
        newUser.setId(5L);
        newUser.setLogin("SernameUpdate");
        newUser.setName("Userupdate");
        newUser.setEmail("update@yangex.com");
        newUser.setBirthday(LocalDate.of(2020, 8, 19));

        Optional<User> userOptional = Optional.ofNullable(userRepository.updateUser(newUser));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 5L);
                            assertThat(user).hasFieldOrPropertyWithValue("login", "SernameUpdate");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "Userupdate");
                            assertThat(user).hasFieldOrPropertyWithValue("email",
                                    "update@yangex.com");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                    LocalDate.of(2020, 8, 19));
                        }
                );
    }

    @Test
    void shouldGetAllFriendsTest() {
        Optional<Collection<User>> usersOptional = Optional.ofNullable(friendRepository.userFriends(1L));
        assertThat(usersOptional)
                .isPresent()
                .hasValueSatisfying(users -> {
                            assertThat(users).isNotEmpty();
                            assertThat(users).hasSize(2);
                            assertThat(users).first().hasFieldOrPropertyWithValue("id", 2L);
                            assertThat(users).element(1).hasFieldOrPropertyWithValue("id", 3L);
                        }
                );
    }

    @Test
    void shouldGetCommonFriendsTest() {
        Optional<Collection<User>> commonFriendsOptional = Optional
                .ofNullable(friendRepository.commonFriends(2L, 3L));
        assertThat(commonFriendsOptional)
                .isPresent()
                .hasValueSatisfying(users -> {
                            assertThat(users).isNotEmpty();
                            assertThat(users).hasSize(2);
                            assertThat(users).element(0).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(users).element(1).hasFieldOrPropertyWithValue("id", 4L);
                        }
                );
    }
}