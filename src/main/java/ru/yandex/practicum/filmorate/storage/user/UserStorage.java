package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> findAllUsers();
    User createUser(User user) throws ValidationException;
    User updateUser(User user) throws ValidationException;
    User getUserById(Integer userId);
    User deleteUser(Integer userId) throws ValidationException;
}