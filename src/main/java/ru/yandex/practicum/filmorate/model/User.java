package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

import lombok.Data;

import java.time.LocalDate;
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

}