package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        return userService.createUser(user);
    }

    @PutMapping //(value = "/users")
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable int id) {
        if (id <= 0) {
            throw new IncorrectParameterException("id");
        }
        return userService.findUser(id);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addToFriendList(@PathVariable int id, @PathVariable int friendId) {
        if (id <= 0 || friendId <= 0) {
            throw new NotFoundException("Пользователя с таким ID не существует.");
        }
        userService.addToFriendList(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void removeFromFriendList(@PathVariable int id, @PathVariable int friendId) {
        if (id <= 0 || friendId <= 0) {
            throw new IncorrectParameterException("id");
        }
        userService.removeFromFriendList(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public List<User> getMutualFriends(@PathVariable int id, @PathVariable int friendId) {
        if (id <= 0 || friendId <= 0) {
            throw new IncorrectParameterException("id");
        }
        return userService.getMutualFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendList(@PathVariable int id) {
        if (id <= 0) {
            throw new IncorrectParameterException("id");
        }
        return userService.getFriendList(id);
    }

}