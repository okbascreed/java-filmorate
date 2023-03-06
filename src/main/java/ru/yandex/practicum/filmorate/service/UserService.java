package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAllUsers(){
        return userStorage.findAllUsers();
    }

    public User createUser(User user) throws ValidationException {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) throws ValidationException {
        return userStorage.updateUser(user);
    }

    public User findUser(int id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("Пользователя с таким ID не существует.");
        }
        return userStorage.getUserById(id);
    }

    public void addToFriendList(int id, int friendId) {
         if(friendId<=0) {
             throw new IncorrectParameterException("ID пользователя не может быть меньше или равен нулю.");
        } else {
            findUser(id).addFriend(friendId);
            findUser(friendId).addFriend(id);
        }
    }

    public void removeFromFriendList(int id, int friendId) {
        findUser(id).deleteFriend(friendId);
        findUser(friendId).deleteFriend(id);
    }

    public List<User> getFriendList(int id) {
        List<User> friends = new ArrayList<>();
        for (int i : findUser(id).getFriends()) {
            friends.add(userStorage.getUsers().get(i));
        }
        return friends;
    }


    public List<User> getMutualFriends(int id, int friendId) {
        List<User> userList;
        Set<Integer> setMutualFriends = new HashSet<>(userStorage.getUserById(id).getFriends());
        setMutualFriends.retainAll(userStorage.getUserById(friendId ).getFriends());
        userList = setMutualFriends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
        return userList;
    }

}