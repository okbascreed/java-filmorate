package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private LikeStorage likeStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            if (userStorage.getUserById(userId) != null) {
                likeStorage.addLike(filmId, userId);
            } else {
                throw new NotFoundException("Пользователь не найден!");
            }
        } else {
            throw new NotFoundException("Фильм c таким ID не найден");
        }
    }

    public void deleteLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            if (film.getLikes().contains(userId)) {
                likeStorage.deleteLike(filmId, userId);
            } else {
                throw new NotFoundException("Лайк от такого пользователя не найден");
            }
        } else {
            throw new NotFoundException("Фильм c таким ID не найден");
        }
    }

    public List<Film> getPopular(Integer count) throws ValidationException {
        if (count < 1) {
            throw new ValidationException("Топ фильмов не может быть отрицательным");
        }
        return likeStorage.getPopular(count);
    }

}