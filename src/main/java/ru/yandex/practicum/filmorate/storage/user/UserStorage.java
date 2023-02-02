package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;

public interface UserStorage {
    HashMap<Integer, User> getUsers();

    List<User> findAllUsers();

    User createUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException;

}
