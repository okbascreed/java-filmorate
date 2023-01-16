package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();
    private List<User> userArr = new ArrayList<>();
    private int id = 1;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        userArr.addAll(users.values());
        return userArr;
    }

    @PostMapping(value = "/users")
    public User addUser(@RequestBody User user) throws ValidationException {
        if (!validate(user)) {
            throw new ValidationException();
        } else {
            user.setId(id);
            id++;
            users.put(user.getId(), user);
        }
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) throws ValidationException {
        try {
            if (users.containsKey(user.getId())) {
                if (!validate(user)) {
                    throw new ValidationException("Не удалось обновить данные пользователя.");
                } else {
                    users.remove(user.getId());
                    users.put(user.getId(), user);
                }
            } else {
                throw new ValidationException("Не удалось обновить данные пользователя.");
            }
        } catch (ValidationException exception) {
            throw new ValidationException(exception.getMessage());
        }
        return user;
    }

    private boolean validate(User user) throws ValidationException {
        boolean validationResult = false;
        try {
            String userName = user.getName();
            String userEmail = user.getEmail();
            String userLogin = user.getLogin();
            if (!userEmail.contains("@")) {
                throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
            } else if (userLogin.contains(" ") || userLogin.equals("")) {
                throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Дата рождения пользователя не может быть в будущем.");
            } else {
                validationResult = true;
                if (userName == null || userName.equals("")) {
                    user.setName(user.getLogin());
                }
            }
        } catch (ValidationException exception) {
            throw new ValidationException(exception.getMessage());
        }
        return validationResult;
    }
}