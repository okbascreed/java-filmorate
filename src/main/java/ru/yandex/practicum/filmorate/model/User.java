package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
@Data
public class User {
    int id = 1;
    @NonNull
    String email;
    @NonNull
    String login;
    String name;
    LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday){
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

}