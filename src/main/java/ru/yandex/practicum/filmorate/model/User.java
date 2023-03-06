package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    int id = 1;
    @Email
    String email;

    @NotBlank
    @NotNull
    String login;
    String name;

    LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday){
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
    private Set<Integer> friends = new HashSet<>();
    public void addFriend(int id) {
        friends.add(id);
    }

    public void deleteFriend(int id){
        friends.remove(id);
    }
}