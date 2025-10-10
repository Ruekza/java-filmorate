package ru.yandex.practicum.filmorate.controller.stogare;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.JdbcUserRepository;

import java.time.LocalDate;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({JdbcUserRepository.class})
public class JdbcUserRepositoryTest {
    private final JdbcUserRepository userRepository;

    @Test
    public void testAddUser() {
        User user = new User(null, "tom@cat.ru", "xxx", "Olga", LocalDate.of(1987, 04, 01));
        User createdUser = userRepository.addUser(user);
        User gotUser = userRepository.getUserById(createdUser.getId());
        Assertions.assertNotNull(createdUser);
        Assertions.assertEquals(createdUser.getId(), gotUser.getId());
        Assertions.assertEquals("tom@cat.ru", createdUser.getEmail());
        Assertions.assertEquals("xxx", createdUser.getLogin());
        Assertions.assertEquals("Olga", createdUser.getName());
        Assertions.assertEquals(LocalDate.of(1987, 04, 01), createdUser.getBirthday());
    }

    @Test
    public void testGetUserById() {
        User user = new User(null, "pochta@mail.ru", "yyy", "Anna", LocalDate.of(1986, 06, 30));
        User createdUser = userRepository.addUser(user);
        User gotUser = userRepository.getUserById(createdUser.getId());
        Assertions.assertNotNull(gotUser);
        Assertions.assertEquals(createdUser.getId(), gotUser.getId());
    }

    @Test
    public void testDeleteUser() {
        User user = new User(null, "ttt@mail.ru", "mmm", "Max", LocalDate.of(1995, 07, 30));
        User createdUser = userRepository.addUser(user);
        User gotUser = userRepository.getUserById(createdUser.getId());
        Assertions.assertNotNull(gotUser);
        userRepository.deleteUser(gotUser.getId());
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userRepository.getUserById(gotUser.getId());
        });
    }

}
