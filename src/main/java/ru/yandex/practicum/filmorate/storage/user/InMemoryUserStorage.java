package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;

@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> users = new HashMap<>();
    private List<User> usersArr = new ArrayList<>();
    private int idCount = 1;

    public HashMap<Integer, User> getUsers() {
        return users;
    }

    public List<User> findAllUsers() {
        usersArr.addAll(users.values());
        return usersArr;
    }

    public User createUser(@Valid User user) throws ValidationException {
        if (!validate(user)) {
            throw new ValidationException();
        } else {
            user.setId(idCount);
            idCount++;
            users.put(user.getId(), user);
        }
        return user;
    }

    public User updateUser(@Valid User user) throws ValidationException {
        try {
            if (users.containsKey(user.getId())) {
                if (!validate(user)) {
                    throw new ValidationException("Не удалось обновить данные пользователя.");
                } else {
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
