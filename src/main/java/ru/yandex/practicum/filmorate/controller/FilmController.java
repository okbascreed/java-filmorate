package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private FilmStorage filmStorage;
    private FilmService filmService;

    @Autowired
    public FilmController(@Qualifier("filmDbStorage") FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmStorage.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        return filmStorage.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(name = "count", defaultValue = "10") Integer count) throws ValidationException {
        return filmService.getPopular(count);
    }

    @ResponseBody
    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        film = filmStorage.addFilm(film);
        return film;
    }

    @ResponseBody
    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        film = filmStorage.updateFilm(film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}")
    public Film delete(@PathVariable Integer id) throws ValidationException {
        return filmStorage.deleteFilm(id);
    }
}