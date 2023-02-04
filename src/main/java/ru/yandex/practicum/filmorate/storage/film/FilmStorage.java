package ru.yandex.practicum.filmorate.storage.film;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;

public interface FilmStorage {
    HashMap<Integer, Film> findAllFilms();
    List<Film> getAllFilmsInList();

    Film addFilm (Film film) throws ValidationException;

    Film updateFilm(Film film) throws ValidationException;

    public Film getFilmById(int id);
}