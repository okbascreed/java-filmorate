package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private List<Film> filmsArr = new ArrayList<>();
    private int id = 1;

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        filmsArr.addAll(films.values());
        return filmsArr;
    }

    @PostMapping(value = "/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        if(!validate(film)) {
            throw new ValidationException();
        } else {
            film.setId(id);
            id++;
            films.put(film.getId(), film);
        }

        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        try{
            if (films.containsKey(film.getId())) {
                if(!validate(film)) {
                    throw new ValidationException("Не удалось обновить!");
                } else {
                    films.remove(film.getId());
                    films.put(film.getId(), film);
                }
            } else {
                throw new ValidationException("Не удалось обновить!");
            }
        } catch (ValidationException exception){
            throw new ValidationException(exception.getMessage());
        }
        return film;
    }

    private boolean validate(Film film) throws ValidationException {
        boolean validationResult = false;
        try{
            String filmName = film.getName();
            if(filmName.equals("")) {
                throw new ValidationException("Название фильма не может быть пустым.");
            } else if (film.getDescription().length() > 200) {
                throw new ValidationException("Максимальная длина описания фильма — 200 символов.");
            } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("Дата релиза фильма не может быть ранее 28 декабря 1895г.");
            } else if (film.getDuration() < 0) {
                throw new ValidationException("Продолжительность фильма должна быть положительной.");
            } else {
                validationResult = true;
            }
        } catch (ValidationException exception) {
            throw new ValidationException(exception.getMessage());
        }
        return validationResult;
    }

}