package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import javax.validation.*;

import java.time.LocalDate;
import java.util.*;

@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> films = new HashMap<>();
    private Integer id = 0;


    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
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

    @Override
    public Film getFilmById(Integer id){
        Film film;
        if (films.containsKey(id)) {
            film = films.get(id);
        } else {
            throw new NotFoundException("Фильм с таким ID не найден.");
        }
        return film;
    }

    @Override
    public Film deleteFilm(Integer filmId) throws ValidationException {
        if (filmId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Фильм с ID=" + filmId + " не найден!");
        }
        return films.remove(filmId);
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