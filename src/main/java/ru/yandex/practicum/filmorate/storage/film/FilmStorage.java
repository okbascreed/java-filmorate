package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilms();
    Film addFilm(Film film) throws ValidationException;
    Film updateFilm(Film film) throws ValidationException;
    Film getFilmById(Integer filmId);
    Film deleteFilm(Integer filmId) throws ValidationException;
}