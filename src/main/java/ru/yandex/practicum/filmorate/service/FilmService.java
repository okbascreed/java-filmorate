package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    Map<Integer, Integer> likes = new HashMap<>();

    List<Film> popularFilms = new ArrayList<>();

    public List<Film> findAllFilms(){
        return filmStorage.getAllFilmsInList();
    }

    public Film addFilm(Film film) throws ValidationException {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) throws ValidationException {
        return filmStorage.updateFilm(film);
    }

    public Film findFilm(int id) {
        if (!filmStorage.findAllFilms().containsKey(id)) {
            throw new NotFoundException("Фильм с таким id не найден");
        }
        return filmStorage.findAllFilms().get(id);
    }

    public void addLike(int id, int userId) {
            findFilm(id).addLike(userId);
    }

    public void deleteLike(int id, int userId) {
        if (userId <= 0) {
            throw new IncorrectParameterException("ID пользователя не может быть меньше или равен нулю.");
        }
        findFilm(id).deleteLike(userId);
    }

    public List<Film> getPopularFilms(int count) {
        popularFilms.clear();
        for (Film film : filmStorage.getAllFilmsInList()) {
            likes.put(film.getId(), film.getLikes().size());
        }

        likes = likes.entrySet()
                .stream().sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(count)
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
        for (Integer id : likes.keySet()) {
            if (popularFilms.isEmpty() || !popularFilms.contains(findFilm(id))) {
                popularFilms.add(findFilm(id));
            }
        }
        return popularFilms;
    }
}

