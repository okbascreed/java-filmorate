package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> films = new HashMap<>();

    public HashMap<Integer, Film> findAllFilms() {
        return films;
    }

    private List<Film> filmsArr = new ArrayList<>();
    private int id = 1;

    public List<Film> getAllFilmsInList() {
        filmsArr.addAll(films.values());
        return filmsArr;
    }

    public Film addFilm(@Valid Film film) throws ValidationException {
        if (!validate(film)) {
            throw new ValidationException();
        } else {
            film.setId(id);
            id++;
            films.put(film.getId(), film);
        }

        return film;
    }

    public Film updateFilm(@Valid Film film) throws ValidationException {
        try {
            if (films.containsKey(film.getId())) {
                if (!validate(film)) {
                    throw new ValidationException("Не удалось обновить!");
                } else {
                    films.put(film.getId(), film);
                }
            } else {
                throw new ValidationException("Не удалось обновить!");
            }
        } catch (ValidationException exception) {
            throw new ValidationException(exception.getMessage());
        }
        return film;
    }


    private boolean validate(Film film) throws ru.yandex.practicum.filmorate.exceptions.ValidationException {
        boolean validationResult = false;
        try {
            String filmName = film.getName();
            if (filmName.isEmpty()) {
                throw new ru.yandex.practicum.filmorate.exceptions.ValidationException("Название фильма не может быть пустым.");
            } else if (film.getDescription().length() > 200) {
                throw new ru.yandex.practicum.filmorate.exceptions.ValidationException("Максимальная длина описания фильма — 200 символов.");
            } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ru.yandex.practicum.filmorate.exceptions.ValidationException("Дата релиза фильма не может быть ранее 28 декабря 1895г.");
            } else if (film.getDuration() < 0) {
                throw new ru.yandex.practicum.filmorate.exceptions.ValidationException("Продолжительность фильма должна быть положительной.");
            } else {
                validationResult = true;
            }
        } catch (ru.yandex.practicum.filmorate.exceptions.ValidationException exception) {
            throw new ru.yandex.practicum.filmorate.exceptions.ValidationException(exception.getMessage());
        }
        return validationResult;
    }

}