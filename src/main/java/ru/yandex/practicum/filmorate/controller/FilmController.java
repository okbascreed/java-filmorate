package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmService.findAllFilms();
    }

    @PostMapping(value = "/films")
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.addFilm(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.updateFilm(film);
    }
    @GetMapping("/films/{id}")
    public Film findFilm(@PathVariable int id) {
        if(id <= 0){
            throw new IncorrectParameterException("id");
        }

        return filmService.findFilm(id);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public void setLike(@PathVariable int id, @PathVariable int userId) {
        if (id <= 0 || userId <= 0) {
            throw new IncorrectParameterException("id");
        }
        filmService.addLike(id, userId);
    }
    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        if(id <=0 || userId <=0 ){
            throw new IncorrectParameterException("id");
        }
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms
            (@RequestParam(value = "count", defaultValue = "10", required = false) String count) {
        if(Integer.parseInt(count) < 0){
            throw new IncorrectParameterException("count");
        }
        return filmService.getPopularFilms(Integer.parseInt(count));
    }

}