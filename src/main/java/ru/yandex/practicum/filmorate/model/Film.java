package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    int id = 1;
    @NotNull
    @NotBlank
    String name;
    @Size(max = 200)
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