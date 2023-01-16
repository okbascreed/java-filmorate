package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class Film {
    int id = 1;
    @NonNull
    String name;
    String description;
    LocalDate releaseDate;
    long duration;

    public Film(String name, String description, LocalDate releaseDate, long duration){
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}